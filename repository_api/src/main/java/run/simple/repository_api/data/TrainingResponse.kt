package run.simple.repository_api.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrainingResponse(
    @SerialName("timer") val timer: TrainingTimer,
)

@Serializable
data class TrainingTimer(
    @SerialName("timer_id") val timerId: Int,
    @SerialName("title") val title: String,
    @SerialName("total_time") val totalTime: Int,
    @SerialName("intervals") val intervals: List<TrainingInterval>,
)

@Serializable
data class TrainingInterval(
    @SerialName("title") val title: String,
    @SerialName("time") val time: Int,
)
