package run.simple.feature_training_screen.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import run.simple.core.navigation.Navigator
import run.simple.feature_training_screen.data.TrainingModel
import run.simple.feature_training_screen.domain.TrainingInteractor
import run.simple.feature_training_screen.domain.TrainingMapper
import run.simple.feature_training_screen.ui.components.intervalItem.TrainingState
import run.simple.repository_api.data.TrainingResponse

class TrainingViewModel(
    private val trainingInteractor: TrainingInteractor,
    private val mapper: TrainingMapper,
    private val navigator: Navigator,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TrainingUiState.default)
    val uiState: StateFlow<TrainingUiState> = _uiState.asStateFlow()

    private val _effect = Channel<TrainingUiEffect>()
    val effect = _effect.receiveAsFlow()

    // Job таймера — храним, чтобы можно было остановить/перезапустить.
    private var tickerJob: Job? = null
    private val model = TrainingModel()

    fun setData(data: TrainingResponse) {
        stopTimer()
        model.data = data.tiny()
        updateUI()
    }

    private fun updateUI() {
        viewModelScope.launch(Dispatchers.Default) {
            val state = mapper.map(model)
            _uiState.update { state }
        }
    }

    fun onAction(action: TrainingUiAction) {
        when (action) {
            TrainingUiAction.OnBackClick -> {
                stopTimer()
                navigator.goBack()
            }

            TrainingUiAction.OnStartClick -> {
                model.start()
                updateUI()
                startTimer()

            }

            TrainingUiAction.OnPauseClick -> {
                model.pause()
                updateUI()
                stopTimer()
            }

            TrainingUiAction.OnResumeClick -> {
                model.resume()
                updateUI()
                startTimer()
            }

            TrainingUiAction.OnResetClick -> {
                model.reset()
                updateUI()
                stopTimer()
            }

            TrainingUiAction.OnNewClick -> {
                navigator.goBack()
            }
        }
    }

    private fun startTimer() {
        stopTimer()
        tickerJob = viewModelScope.launch {
            while (isActive && model.currentProgress < 1f) {
                val current = currentTimeMillis()
                delay(TIMER_PERIOD_MS)
                model.increment(passedMs = currentTimeMillis() - current)
                updateUI()

            }
        }
    }

    private fun stopTimer() {
        tickerJob?.cancel()
        tickerJob = null
    }

    private fun currentTimeMillis(): Long = System.currentTimeMillis()

    override fun onCleared() {
        stopTimer()
        super.onCleared()
    }

    private companion object {

        const val TIMER_PERIOD_MS = 1_000L
    }
}

private fun TrainingResponse.tiny(): TrainingResponse {
    var total = 0
    val items = timer.intervals.map { item ->
        val time = item.time / 10
        total += time
        item.copy(time = time)
    }
    return copy(
        timer = timer.copy(
            intervals = items,
            totalTime = total
        )
    )
}
