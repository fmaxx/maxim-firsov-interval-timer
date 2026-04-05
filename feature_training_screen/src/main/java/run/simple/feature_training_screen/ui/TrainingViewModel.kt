package run.simple.feature_training_screen.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

class TrainingViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(TrainingUiState())
    val uiState: StateFlow<TrainingUiState> = _uiState.asStateFlow()

    private val _effect = Channel<TrainingUiEffect>()
    val effect = _effect.receiveAsFlow()

    fun onAction(action: TrainingUiAction) {
        when (action) {
            TrainingUiAction.OnStartClick -> _uiState.update { it.copy(isRunning = true) }
            TrainingUiAction.OnPauseClick -> _uiState.update { it.copy(isRunning = false) }
            TrainingUiAction.OnResetClick -> _uiState.update {
                it.copy(isRunning = false, currentIntervalTimeLeft = 0)
            }
        }
    }
}
