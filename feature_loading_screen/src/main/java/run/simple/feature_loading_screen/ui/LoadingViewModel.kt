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
import run.simple.core.navigation.Navigator
import run.simple.core.navigation.TrainingRoute
import run.simple.repository_api.FetchTrainingResult
import run.simple.repository_api.TrainingRepository
import timber.log.Timber

class LoadingViewModel(
    private val trainingRepository: TrainingRepository,
    private val navigator: Navigator,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoadingUiState())
    val uiState: StateFlow<LoadingUiState> = _uiState.asStateFlow()

    private val _effect = Channel<LoadingUiEffect>()
    val effect = _effect.receiveAsFlow()

    fun onAction(action: LoadingUiAction) {
        when (action) {
            is LoadingUiAction.OnLoadClick -> fetchData(uiState.value.trainingId)
            is LoadingUiAction.OnInputChanged -> {
                _uiState.update {
                    it.copy(
                        isError = false,
                        trainingInput = action.value,
                    )
                }
            }
        }
    }

    private fun fetchData(trainingId: Int) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    isError = false,
                )
            }
            when (val result = trainingRepository.fetchTraining(id = trainingId)) {
                is FetchTrainingResult.Success -> {
                    Timber.d("success: ${result.response}")
                    _uiState.update { it.copy(isLoading = false, isError = false) }
                    navigator.goTo(TrainingRoute(result.response))
                }

                is FetchTrainingResult.Fail -> {
                    val message = result.throwable.message ?: "Unknown error"
                    Timber.d("fail: $message")
                    _uiState.update { it.copy(isLoading = false, isError = true) }
                    _effect.send(LoadingUiEffect.ShowError(message))
                }
            }
        }
    }
}