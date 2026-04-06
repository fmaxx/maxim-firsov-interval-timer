package run.simple.feature_training_screen.domain

import kotlinx.collections.immutable.toImmutableList
import run.simple.core.util.toTimerFormat
import run.simple.feature_training_screen.data.TrainingModel
import run.simple.feature_training_screen.ui.IntervalsViewState
import run.simple.feature_training_screen.ui.TopbarState
import run.simple.feature_training_screen.ui.TrainingCardState
import run.simple.feature_training_screen.ui.TrainingUiState
import run.simple.feature_training_screen.ui.components.buttons.ButtonsState
import run.simple.feature_training_screen.ui.components.intervalItem.IntervalItemState
import run.simple.feature_training_screen.ui.components.intervalItem.TrainingState
import timber.log.Timber

class TrainingMapper {

    /**
     * Маппинг UiState из модели данных тренировки
     * */
    fun map(model: TrainingModel): TrainingUiState = TrainingUiState(
        topBarState = topbarState(model),
        buttonsState = buttonsState(model),
        trainingCardState = trainingCardState(model),
        intervalsViewState = intervalsViewState(model),
    )

    private fun topbarState(
        model: TrainingModel,
    ): TopbarState {
        val trainingState = model.trainingState
        val totalSeconds = model.totalSeconds
        return TopbarState(
            title = model.data.timer.title,
            trainingState = trainingState,
            status = when (trainingState) {
                TrainingState.Idle -> totalSeconds.toTimerFormat()
                is TrainingState.Running -> {
                    val leftSec = totalSeconds - model.currentSeconds
                    "● ${leftSec.toTimerFormat()}"
                }

                is TrainingState.Pause -> "❚❚ Пауза"
                TrainingState.Completed -> "Завершена"
            }
        )
    }

    private fun buttonsState(model: TrainingModel): ButtonsState =
        ButtonsState(model.trainingState)

    private fun trainingCardState(model: TrainingModel): TrainingCardState {
        val trainingState = model.trainingState
        val totalTimeSec = model.totalSeconds
        return TrainingCardState(
            trainingState = trainingState,
            status = when (trainingState) {
                TrainingState.Idle -> "ГОТОВО К СТАРТУ"
                is TrainingState.Running -> "ВЫПОЛНЯЕТСЯ"
                is TrainingState.Pause -> "НА ПАУЗЕ"
                TrainingState.Completed -> "ТРЕНИРОВКА ЗАВЕРШЕНА"
            },
            trainingName = if (trainingState == TrainingState.Completed) {
                "Отличная работа!"
            } else {
                val current = model.currentInterval
                current?.interval?.title ?: model.data.timer.title
            },

            currentTimeLeft = when (trainingState) {
                TrainingState.Idle -> totalTimeSec.toTimerFormat()
                is TrainingState.Pause,
                is TrainingState.Running,
                    -> {
                    val currentInterval = model.currentInterval
                    if (currentInterval == null) {
                        ""
                    } else {
                        val leftLocal = currentInterval.endTimeSec - model.currentSeconds
                        leftLocal.toTimerFormat()
                    }
                }

                TrainingState.Completed -> "00:00"
            },

            totalProgressMessage = when (trainingState) {
                TrainingState.Idle -> "Общее время"
                is TrainingState.Pause,
                is TrainingState.Running,
                    -> {
                    "Прошло ${model.currentSeconds.toTimerFormat()} из ${model.totalSeconds.toTimerFormat()}"
                }

                TrainingState.Completed -> "${model.currentSeconds.toTimerFormat()} из ${model.totalSeconds.toTimerFormat()}"
            },
        )
    }

    private fun intervalsViewState(
        model: TrainingModel,
    ): IntervalsViewState {
        val globalTrainingState = model.trainingState
        val intervals = model.intervalWithTimings
        val globalPosition = model.currentSeconds
        val items = intervals.mapIndexed { index, item ->
            val isIntervalActive = globalPosition in item.startTimeSec..item.endTimeSec
            val itemTrainingState = if (isIntervalActive) {
                val localProgress = (globalPosition - item.startTimeSec).toFloat() / item.interval.time.toFloat()

                when(globalTrainingState){
                    TrainingState.Idle -> TrainingState.Running(localProgress)
                    TrainingState.Completed -> TrainingState.Completed
                    is TrainingState.Pause -> TrainingState.Pause(localProgress)
                    is TrainingState.Running -> TrainingState.Running(localProgress)
                }
               /* if (globalTrainingState is TrainingState.Pause) {
                    TrainingState.Pause(localProgress)
                } else {

                    TrainingState.Running(localProgress)
                }*/
            } else {
                val isPassed = globalPosition > item.endTimeSec
                if (isPassed) {
                    TrainingState.Completed
                } else {
                    TrainingState.Idle
                }
            }
            IntervalItemState(
                trainingState = itemTrainingState,
                order = (index + 1).toString(),
                title = item.interval.title,
                left = when (itemTrainingState) {
                    TrainingState.Completed,
                    TrainingState.Idle,
                        -> item.interval.time.toTimerFormat()

                    is TrainingState.Pause,
                    is TrainingState.Running,
                        -> (item.endTimeSec - globalPosition).toTimerFormat()
                }
            )
        }

        val numCompleted = items.fold(0) { acc, item ->
            if (item.trainingState == TrainingState.Completed) {
                acc + 1
            } else {
                acc
            }
        }

        return IntervalsViewState(
            items = items.toImmutableList(),
            progress = when {
                numCompleted == items.size -> {
                    "$numCompleted из $numCompleted ✓"
                }

                numCompleted > 0 -> {
                    "$numCompleted из ${items.size}"
                }

                numCompleted == 0 -> {
                    "${items.size} интервалов"
                }

                else -> ""
            },
        )
    }
}