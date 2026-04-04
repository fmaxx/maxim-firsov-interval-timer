package run.simple.feature_loading_screen.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import run.simple.repository_api.FetchTrainingResult
import run.simple.repository_api.TrainingRepository

class LoadingViewModel(
    private val trainingRepository: TrainingRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoadingUiState())
    val uiState: StateFlow<LoadingUiState> = _uiState.asStateFlow()

    private val _effect = Channel<LoadingUiEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        fetchData()
    }

    fun onAction(action: LoadingUiAction) {
        when (action) {
            LoadingUiAction.Retry -> fetchData()
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = trainingRepository.fetchTraining(id = TRAINING_ID)) {
                is FetchTrainingResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    // _effect.send(LoadingUiEffect.NavigateNext)
                }

                is FetchTrainingResult.Fail -> {
                    val message = result.throwable.message ?: "Unknown error"
                    _uiState.update { it.copy(isLoading = false, errorMessage = message) }
                    // _effect.send(LoadingUiEffect.ShowError(message))
                }
            }
        }
    }

    private companion object {

        const val TRAINING_ID = 68
    }
}
