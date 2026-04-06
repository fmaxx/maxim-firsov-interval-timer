package run.simple.feature_training_screen.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import run.simple.feature_training_screen.domain.TrainingInteractor
import run.simple.repository_api.data.TrainingResponse

class TrainingViewModel(
    private val trainingInteractor: TrainingInteractor,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TrainingUiState.default)
    val uiState: StateFlow<TrainingUiState> = _uiState.asStateFlow()

    private val _effect = Channel<TrainingUiEffect>()
    val effect = _effect.receiveAsFlow()

    // Job таймера — храним, чтобы можно было остановить/перезапустить.
    private var tickerJob: Job? = null

    fun setData(data: TrainingResponse) {
        trainingInteractor.setData(data)
    }

    fun onAction(action: TrainingUiAction) {
        /* when (action) {
             TrainingUiAction.OnStartClick -> _uiState.update {
                 it.copy(isRunning = true)
             }
             TrainingUiAction.OnPauseClick -> _uiState.update {
                 it.copy(isRunning = false)
             }
             TrainingUiAction.OnResetClick -> _uiState.update {
                 it.copy(isRunning = false, currentIntervalTimeLeft = 0)
             }
         }*/
    }

    private fun launchTicker() {
        tickerJob?.cancel()
        tickerJob = viewModelScope.launch {
            trainingInteractor.tick()
            while (isActive) {
                delay(500L)
                trainingInteractor.tick()
            }
        }
    }

    override fun onCleared() {
        tickerJob?.cancel()
        super.onCleared()
    }
}
