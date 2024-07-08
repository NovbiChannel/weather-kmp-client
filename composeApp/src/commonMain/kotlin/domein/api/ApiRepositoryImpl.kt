package domein.api

import Greeting
import domein.api.ApiRoutes.GET_WEATHER
import domein.api.ApiRoutes.POST_ERROR
import domein.api.ApiRoutes.POST_EVENT
import domein.models.MetricErrorModel
import domein.models.MetricEventModel
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.network.sockets.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.novbicreate.utils.Resource
import domein.models.WeatherModel
import java.net.ConnectException

class ApiRepositoryImpl: ApiRepository {
    private val _client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }
    override suspend fun getWeather(): Resource<WeatherModel> {
        try {
            val city = "Miass"
            val result = _client.get(GET_WEATHER) {
                parameter("language", "russian")
                parameter("city", city)
            }
            sendEvent("Показ погоды для города: $city")
            return Resource.Success(result.body())
        } catch (e: Exception) {
            e.printStackTrace()
            val message = when (e) {
                is SocketTimeoutException -> "Время ожидание ответа истекло"
                is ConnectException -> "Связь с сервером не установлена"
                else -> "Неизвестная ошибка"
            }
            sendError(e.localizedMessage)
            return Resource.Error(message)
        }
    }

    private suspend fun sendEvent(details: String) {
        try {
            _client.post(POST_EVENT) {
                contentType(ContentType.Application.Json)
                setBody(MetricEventModel(
                    "client",
                    Greeting().platformName(),
                    System.currentTimeMillis(),
                    details
                ))
            }
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun sendError(errorMessage: String) {
        try {
            _client.post(POST_ERROR) {
                contentType(ContentType.Application.Json)
                setBody(MetricErrorModel(
                    "client",
                    Greeting().platformName(),
                    System.currentTimeMillis(),
                    errorMessage
                ))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}