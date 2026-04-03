package run.simple.repository_api.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TimerResponse(
    @SerialName("timer") val timer: Timer,
)

@Serializable
data class Timer(
    @SerialName("timer_id") val timerId: Int,
    @SerialName("title") val title: String,
    @SerialName("total_time") val totalTime: Int,
    @SerialName("intervals") val intervals: List<Interval>,
)

@Serializable
data class Interval(
    @SerialName("title") val title: String,
    @SerialName("time") val time: Int,
)
