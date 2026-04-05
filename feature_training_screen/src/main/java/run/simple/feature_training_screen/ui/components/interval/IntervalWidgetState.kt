package run.simple.feature_training_screen.ui.components.interval

data class IntervalWidgetState(
    val trainingState: TrainingState,
    val order: String,
    val title: String,
    val left: String,
)

sealed interface TrainingState {
    data object Idle : TrainingState
    data class Running(
        // 0.0 ... 1.0
        val progress: Float,
    ) : TrainingState

    data class Pause(
        // 0.0 ... 1.0
        val progress: Float,
    ) : TrainingState

    data object Completed : TrainingState
}
