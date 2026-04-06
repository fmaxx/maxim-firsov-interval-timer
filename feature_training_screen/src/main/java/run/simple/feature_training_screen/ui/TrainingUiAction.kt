package run.simple.feature_training_screen.ui

sealed interface TrainingUiAction {
    data object OnStartClick : TrainingUiAction
    data object OnPauseClick : TrainingUiAction
    data object OnResetClick : TrainingUiAction
    data object OnBackClick : TrainingUiAction
    data object OnResumeClick : TrainingUiAction
    data object OnNewClick : TrainingUiAction
}
