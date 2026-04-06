package run.simple.feature_training_screen.ui

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import run.simple.feature_training_screen.ui.components.buttons.ButtonsState
import run.simple.feature_training_screen.ui.components.intervalItem.IntervalItemState
import run.simple.feature_training_screen.ui.components.intervalItem.TrainingState

@Immutable
data class TrainingUiState(
    val topBarState: TopbarState,
    val buttonsState: ButtonsState,
    val trainingCardState: TrainingCardState,
    val intervalsViewState: IntervalsViewState,
) {

    companion object {

        val default = TrainingUiState(
            topBarState = TopbarState.default,
            buttonsState = ButtonsState.default,
            trainingCardState = TrainingCardState.default,
            intervalsViewState = IntervalsViewState.default,
        )
    }
}

@Immutable
data class TopbarState(
    val title: String = "",
    val totaLeftTime: String = "00:00",
    val trainingState: TrainingState = TrainingState.Idle,
) {

    companion object {

        val default = TopbarState(
            title = "",
            totaLeftTime = "",
            trainingState = TrainingState.Idle,
        )
    }
}

@Immutable
data class TrainingCardState(
    val trainingState: TrainingState,
    val status: String,
    val trainingName: String,
    val currentTimeLeft: String,
    val totalProgressMessage: String,
) {

    val totalProgress: Float
        get() = when (trainingState) {
            TrainingState.Completed -> 1f
            TrainingState.Idle -> 0f
            is TrainingState.Pause -> trainingState.progress
            is TrainingState.Running -> trainingState.progress
        }

    companion object {

        val default = TrainingCardState(
            trainingState = TrainingState.Idle,
            status = "",
            trainingName = "",
            currentTimeLeft = "",
            totalProgressMessage = "",
        )
    }
}

@Immutable
data class IntervalsViewState(
    val items: ImmutableList<IntervalItemState>,
    val progress: String,
) {

    companion object {

        val default = IntervalsViewState(
            items = persistentListOf(),
            progress = ""
        )
    }
}

@Immutable
data class StatsViewState(
    val totalTime: String,
    val totalIntervals: Int,
) {

    companion object {

        val default: StatsViewState = StatsViewState(
            totalTime = "",
            totalIntervals = 0,
        )
    }
}

