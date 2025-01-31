package domein.models

import kotlinx.serialization.Serializable

@Serializable
data class MetricEventModel(
    val type: String,
    val source: String,
    val time: Long,
    val details: String,
)
