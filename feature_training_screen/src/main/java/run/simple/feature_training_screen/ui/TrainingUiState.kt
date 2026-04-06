package run.simple.feature_training_screen.ui

import androidx.compose.runtime.Immutable
import run.simple.feature_training_screen.ui.components.intervalItem.TrainingState

@Immutable
data class TrainingUiState(
    val title: String = "",
    val totaLeftTime: String = "00:00",
    val currentIntervalTitle: String = "",
    val currentIntervalTimeLeft: Int = 0,
    val isRunning: Boolean = false,
    val trainingState: TrainingState = TrainingState.Idle,
)

@Immutable
data class TrainingCardState(
    val trainingState: TrainingState,
    val status: String,
    val trainingName: String,
    val currentTimeLeft: String,
    val totalProgressMessage: String,
) {

    val totalProgress: Float
        get() = when (trainingState) {
            TrainingState.Completed -> 1f
            TrainingState.Idle -> 0f
            is TrainingState.Pause -> trainingState.progress
            is TrainingState.Running -> trainingState.progress
        }
}

@Immutable
data class StatsViewState(
    val totalTime: String,
    val totalIntervals: Int,
)

