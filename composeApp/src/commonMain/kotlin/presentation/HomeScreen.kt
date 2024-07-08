package presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domein.api.ApiRepositoryImpl
import domein.models.WeatherModel
import kotlinx.coroutines.launch
import org.novbicreate.utils.Resource
import java.util.*

@Composable
fun HomeScreen() {
    val repository = ApiRepositoryImpl()
    val apiResponse = remember { mutableStateOf (loadingData()) }
    val scope = rememberCoroutineScope()
    MaterialTheme {
        Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
            scope.launch {
                val weather = repository.getWeather()
                when (weather) {
                    is Resource.Error -> apiResponse.value = loadingData()
                    is Resource.Success -> apiResponse.value = weather.data?: loadingData()
                }
            }
            BuildWeatherUI(apiResponse.value)
        }
    }
}
@Composable
private fun BuildWeatherUI(weather: WeatherModel) {
    val city = weather.city
    val conditions = weather.conditions.replaceFirstChar { it.uppercase() }
    val temperature = "${weather.temperature}°C"
    val humidity = "Влажность ${weather.humidity}%"
    val windSpeed = if (weather.windSpeed!= 0) "Ветер ${weather.windSpeed} м/с" else ""
    Column(Modifier.fillMaxWidth()) {
        Text("Сегодня")
        Spacer(Modifier.height(8.dp))
        Text(fontSize = 32.sp, text = city, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text(todayDate())
        Spacer(Modifier.height(16.dp))
        Column(Modifier.wrapContentSize()) {
            Text(fontSize = 55.sp, text = temperature, fontWeight = FontWeight.Bold)
            Text(conditions)
        }
        Spacer(Modifier.height(8.dp))
        Text(humidity)
        Text(windSpeed)
    }
}
private fun todayDate(): String {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1 // месяцы считаются с 0
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    return String.format("%02d.%02d.%d", day, month, year)
}
private fun loadingData(): WeatherModel {
    return WeatherModel(
        "Загружаем данные...",
        "",
        0,
        0,
        0
    )
}