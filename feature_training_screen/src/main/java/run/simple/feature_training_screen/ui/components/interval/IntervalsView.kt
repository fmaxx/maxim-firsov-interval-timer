package run.simple.feature_training_screen.ui.components.interval

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.persistentListOf
import run.simple.core.theme.DemoColors
import run.simple.feature_training_screen.ui.IntervalsViewState
import run.simple.feature_training_screen.ui.components.intervalItem.IntervalItemState
import run.simple.feature_training_screen.ui.components.intervalItem.IntervalItemView
import run.simple.feature_training_screen.ui.components.intervalItem.TrainingState

@Composable
fun IntervalsView(
    state: IntervalsViewState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Text(
                text = "Интервалы",
                modifier = Modifier.weight(1f),
                color = DemoColors.Black,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = state.progress,
                color = DemoColors.TextGray,
            )

        }
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            itemsIndexed(
                items = state.items,
                key = { _, item -> item.order }
            ) { _, item ->
                IntervalItemView(state = item)
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F5F5, widthDp = 360)
@Composable
private fun IntervalsItemViewPreview() {
    MaterialTheme {
        IntervalsView(
            state = IntervalsViewState(
                items = persistentListOf(
                    IntervalItemState(
                        trainingState = TrainingState.Idle,
                        order = "1",
                        title = "Ходьба в среднем темпе",
                        left = "5:00"
                    ),

                    IntervalItemState(
                        trainingState = TrainingState.Running(0.5f),
                        order = "2",
                        title = "Ходьба в среднем темпе",
                        left = "5:00"
                    ),

                    IntervalItemState(
                        trainingState = TrainingState.Pause(0.75f),
                        order = "3",
                        title = "Ходьба в среднем темпе",
                        left = "5:00"
                    ),

                    IntervalItemState(
                        trainingState = TrainingState.Completed,
                        order = "4",
                        title = "Ходьба в среднем темпе",
                        left = "5:00"
                    ),
                ),
                progress = "7 из 7 ✓"
            )
        )
    }
}