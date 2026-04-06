package run.simple.feature_training_screen.ui.components.intervalItem

data class IntervalItemState(
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
