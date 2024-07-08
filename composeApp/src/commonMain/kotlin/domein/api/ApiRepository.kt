package domein.api

import org.novbicreate.utils.Resource
import domein.models.WeatherModel

interface ApiRepository {
    suspend fun getWeather(): Resource<WeatherModel>
}