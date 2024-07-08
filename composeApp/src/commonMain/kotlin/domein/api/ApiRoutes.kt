package domein.api

object ApiRoutes {
    private const val BASE_URL = "http://192.168.0.101:8080"
    private const val BASE_METRIC_URL = "http://192.168.0.101:8081"
    const val GET_WEATHER = "$BASE_URL/weather"
    const val POST_EVENT = "$BASE_METRIC_URL/event_collection"
    const val POST_ERROR = "$BASE_METRIC_URL/error_collection"
}