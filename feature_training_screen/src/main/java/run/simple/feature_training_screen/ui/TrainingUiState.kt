package run.simple.feature_training_screen.ui

import androidx.compose.runtime.Immutable

@Immutable
data class TrainingUiState(
    val title: String = "",
    val totalTime: Int = 0,
    val currentIntervalTitle: String = "",
    val currentIntervalTimeLeft: Int = 0,
    val isRunning: Boolean = false,
)
