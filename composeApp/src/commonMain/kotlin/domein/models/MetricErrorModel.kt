package domein.models

import kotlinx.serialization.Serializable

@Serializable
data class MetricErrorModel(
    val type: String,
    val source: String,
    val time: Long,
    val description: String,
)
