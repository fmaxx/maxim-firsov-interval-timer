package run.simple.feature_training_screen.ui

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.AndroidViewModel
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
import run.simple.feature_training_screen.data.TrainingIntervalWithTimings
import run.simple.feature_training_screen.data.TrainingModel
import run.simple.feature_training_screen.domain.TrainingMapper
import run.simple.feature_training_screen.sound.TrainingService
import run.simple.feature_training_screen.sound.TrainingServiceCommand
import run.simple.feature_training_screen.sound.TrainingServiceInteractor
import run.simple.feature_training_screen.ui.components.intervalItem.TrainingState
import run.simple.repository_api.data.TrainingResponse

class TrainingViewModel(
    private val mapper: TrainingMapper,
    private val navigator: Navigator,
    application: Application,
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(TrainingUiState.default)
    val uiState: StateFlow<TrainingUiState> = _uiState.asStateFlow()

    private val _effect = Channel<TrainingUiEffect>()
    val effect = _effect.receiveAsFlow()

    // Job таймера — храним, чтобы можно было остановить/перезапустить.
    private var tickerJob: Job? = null
    private val model = TrainingModel()

    // Training Service
    private var serviceInteractor: TrainingServiceInteractor? = null
    private var isBound = false
    private var pendingStartSound = false

    // Коннектор для работы с сервисом
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            val localBinder = binder as? TrainingService.TrainingServiceBinder ?: return
            serviceInteractor = localBinder.getService()
            isBound = true
            if (pendingStartSound) {
                pendingStartSound = false
                serviceInteractor?.run(TrainingServiceCommand.PlayStart)
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            serviceInteractor = null
            isBound = false
        }
    }

    init {
        TrainingService.start(getApplication())
        bindService()
    }

    fun setData(data: TrainingResponse) {
        stopTimer()
        model.data = data
        updateUI()
    }

    private fun bindService() {
        if (isBound) return
        val context = getApplication<Application>()
        val intent = Intent(context, TrainingService::class.java)
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun unbindService() {
        if (!isBound) return
        getApplication<Application>().unbindService(serviceConnection)
        serviceInteractor = null
        isBound = false
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
                serviceInteractor?.run(TrainingServiceCommand.PlayStart)
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
                stopTimer()
                navigator.goBack()
            }
        }
    }

    private fun startTimer() {
        stopTimer()
        tickerJob = viewModelScope.launch {
            while (isActive && model.currentProgress < 1f) {
                val intervalBefore = model.currentInterval
                val current = currentTimeMillis()
                delay(TIMER_PERIOD_MS)
                model.increment(passedMs = currentTimeMillis() - current)
                checkIntervals(
                    before = intervalBefore,
                    after = model.currentInterval
                )
                updateUI()
            }
        }
    }

    private fun checkIntervals(
        before: TrainingIntervalWithTimings?,
        after: TrainingIntervalWithTimings?,
    ) {
        if (before != after) {
            if (model.trainingState == TrainingState.Completed) {
                // тренировка окончена
                serviceInteractor?.run(TrainingServiceCommand.PlayFinish)
            } else {
                // переключился интервал
                serviceInteractor?.run(TrainingServiceCommand.PlayInterval)
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
        unbindService()
        TrainingService.stop(getApplication())
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
