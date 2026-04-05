package run.simple.feature_training_screen.ui

sealed interface TrainingUiEffect {
    data class ShowError(val message: String) : TrainingUiEffect
    data object TrainingFinished : TrainingUiEffect
}
