package run.simple.feature_training_screen.data

import run.simple.feature_training_screen.ui.components.intervalItem.TrainingState
import run.simple.repository_api.data.TrainingInterval
import run.simple.repository_api.data.TrainingResponse
import run.simple.repository_api.data.TrainingTimer
import timber.log.Timber

class TrainingModel {

    val time = TimeModel()
    var data: TrainingResponse = TrainingResponse(
        timer = TrainingTimer(timerId = 0, title = "", totalTime = 1, intervals = emptyList())
    )
        set(value) {
            field = value
            var position = 0
            _intervalWithTimings.clear()
            _intervalWithTimings.addAll(
                elements = value.timer.intervals.map {
                    val startTimeSec = position
                    val endTimeSec = position + it.time
                    position = endTimeSec
                    TrainingIntervalWithTimings(
                        interval = it,
                        startTimeSec = startTimeSec,
                        endTimeSec = endTimeSec,
                    )
                }.also {
                    print(it)
                }
            )
        }
    var trainingState: TrainingState = TrainingState.Idle
    val currentSeconds: Int get() = (time.currentMs / 1_000).toInt()
    val currentProgress: Float get() = currentSeconds.toFloat() / totalSeconds.toFloat()
    val totalSeconds: Int
        get() = data.timer.totalTime

    private val _intervalWithTimings = mutableListOf<TrainingIntervalWithTimings>()
    val intervalWithTimings: List<TrainingIntervalWithTimings> get() = _intervalWithTimings.toList()

    val currentInterval: TrainingIntervalWithTimings?
        get() {
            val position = currentSeconds

            return _intervalWithTimings.firstOrNull {
                position in it.startTimeSec..<it.endTimeSec
            }
        }

    fun start() {
        time.currentMs = 0
        trainingState = TrainingState.Running(progress = currentProgress)
    }

    fun increment(passedMs: Long) {
        time.increment(passedMs)
        if (currentProgress < 1f) {
            updateProgress()
        } else {
            trainingState = TrainingState.Completed
        }
    }

    private fun updateProgress() {
        trainingState = when (trainingState) {
            TrainingState.Idle,
            TrainingState.Completed,
                -> trainingState

            is TrainingState.Pause -> TrainingState.Pause(currentProgress)
            is TrainingState.Running -> TrainingState.Running(currentProgress)
        }
    }

    fun pause() {
        trainingState = TrainingState.Pause(progress = currentProgress)
    }

    fun resume() {
        trainingState = TrainingState.Running(progress = currentProgress)
    }

    fun reset() {
        time.currentMs = 0
        trainingState = TrainingState.Idle
    }
}

data class TrainingIntervalWithTimings(
    val interval: TrainingInterval,
    val startTimeSec: Int,
    val endTimeSec: Int,
)