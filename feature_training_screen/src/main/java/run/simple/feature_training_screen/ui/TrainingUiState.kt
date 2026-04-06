package run.simple.feature_training_screen.ui

import androidx.compose.runtime.Immutable

@Immutable
data class TrainingUiState(
    val title: String = "",
    val totalTime: String = "00:00",
    val currentIntervalTitle: String = "",
    val currentIntervalTimeLeft: Int = 0,
    val isRunning: Boolean = false,
)
