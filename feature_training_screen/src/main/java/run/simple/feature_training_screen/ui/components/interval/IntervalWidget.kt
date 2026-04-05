package run.simple.feature_training_screen.ui.components.interval

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Green500 = Color(0xFF4CAF50)
private val BorderGray = Color(0xFFE5E7EB)

@Composable
fun IntervalWidget(
    state: IntervalWidgetState,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = BorderStroke(1.5.dp, BorderGray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconWidget(state)

            Spacer(Modifier.width(14.dp))

            TitleWidget(state)

            LeftWidget(state)
        }
    }
}

@Composable
private fun LeftWidget(state: IntervalWidgetState) {
    val textColor = when (state.trainingState) {
        TrainingState.Idle -> Color(0xFF5F6776)
        TrainingState.Completed -> TODO()
        is TrainingState.Pause -> TODO()
        is TrainingState.Running -> TODO()
    }
    Text(
        text = state.left,
        color = textColor,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun RowScope.TitleWidget(state: IntervalWidgetState) {
    Text(
        text = state.title,
        color = Color.Black,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.weight(1f)
    )
}

@Composable
private fun IconWidget(state: IntervalWidgetState) {
    val orderTextColor = when (state.trainingState) {
        TrainingState.Idle -> Color(0xFF939BAA)
        TrainingState.Completed -> TODO()
        is TrainingState.Pause -> TODO()
        is TrainingState.Running -> TODO()
    }

    val orderBgdColor = when (state.trainingState) {
        TrainingState.Idle -> Color(0xFFEDEDEF)
        TrainingState.Completed -> TODO()
        is TrainingState.Pause -> TODO()
        is TrainingState.Running -> TODO()
    }
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(orderBgdColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = state.order,
            color = orderTextColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F5F5, widthDp = 360)
@Composable
private fun IntervalWidgetPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            IntervalWidget(
                state = IntervalWidgetState(
                    trainingState = TrainingState.Idle,
                    order = "1",
                    title = "Ходьба в среднем темпе",
                    left = "5:00"
                )
            )
//            IntervalItem(index = 1, title = "Ходьба в среднем темпе", timeSeconds = 300)
//            IntervalItem(index = 2, title = "Ходьба в интенсивном темпе", timeSeconds = 300)
//            IntervalItem(index = 3, title = "Ходьба в среднем темпе", timeSeconds = 120)
//            IntervalItem(index = 4, title = "Медленный бег", timeSeconds = 30)
        }
    }
}