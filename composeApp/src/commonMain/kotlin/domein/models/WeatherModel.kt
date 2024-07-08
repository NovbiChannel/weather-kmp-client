package domein.models

import kotlinx.serialization.Serializable

@Serializable
data class WeatherModel(
    val city: String,
    val conditions: String,
    val temperature: Int,
    val humidity: Int,
    val windSpeed: Int
)
