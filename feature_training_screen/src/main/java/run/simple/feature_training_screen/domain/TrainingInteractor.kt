package run.simple.feature_training_screen.domain

import run.simple.feature_training_screen.ui.components.intervalItem.TrainingState
import run.simple.repository_api.data.TrainingResponse
import run.simple.repository_api.data.TrainingTimer
import timber.log.Timber

class TrainingInteractor {

    private var data: TrainingResponse =
        TrainingResponse(timer = TrainingTimer(timerId = 0, title = "", totalTime = 1, intervals = emptyList()))

    private var status: TrainingState = TrainingState.Idle
    private var lastUpdatedMs: Long = 0
    private var currentMilliseconds: Long = 0
    private val totalSeconds: Long get() = data.timer.totalTime.toLong()

    val currentSeconds: Int get() = (currentMilliseconds / 1_000).toInt()

    val currentProgress: Float get() = currentSeconds.toFloat() / totalSeconds.toFloat()

    fun setData(data: TrainingResponse) {
        this.data = data
        reset()
    }

    fun start() {
        if (status is TrainingState.Running) {
            Timber.w("state is already playing")
            return
        }
        status = TrainingState.Running(currentProgress)
        lastUpdatedMs = currentTimeMillis()
    }

    fun pause() {
        if (status is TrainingState.Pause) {
            Timber.w("state is already paused")
            return
        }
        status = TrainingState.Pause(currentProgress)
        lastUpdatedMs = currentTimeMillis()
    }

    fun reset() {
        lastUpdatedMs = currentTimeMillis()
        status = TrainingState.Idle
        currentMilliseconds = 0L
    }

    fun tick() {
        if (totalSeconds <= 0) {
            Timber.w("totalDuration less 0")
            return
        }

        if (status !is TrainingState.Running) {
            Timber.w("state isn't playing")
            return
        }

        currentMilliseconds += (lastUpdatedMs - currentTimeMillis())
        lastUpdatedMs = currentTimeMillis()
    }

    private fun currentTimeMillis(): Long = System.currentTimeMillis()
}