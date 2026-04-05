package run.simple.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import run.simple.repository_api.data.TimerResponse

@Serializable
object LoadingRoute : NavKey

@Serializable
data class TrainingRoute(val data: TimerResponse) : NavKey