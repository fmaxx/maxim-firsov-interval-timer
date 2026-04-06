package run.simple.feature_training_screen.ui.components.interval

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Green500 = Color(0xFF4CAF50)
private val BorderGray = Color(0xFFE5E7EB)
private val GreenFill = Color(0xFFBFE4C4)   // цвет залитой части (светло-зелёный)
private val Orange = Color(0xFFE8883A)
private val OrangeFill = Color(0xFFFBE9D7)

@Composable
fun IntervalWidget(
    state: IntervalWidgetState,
    modifier: Modifier = Modifier,
) {

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = BorderStroke(1.dp, state.borderColor()),
        shadowElevation = 2.dp

    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {

            ProgressView(state)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconView(state)

                Spacer(Modifier.width(14.dp))

                TitleView(state)

                LeftTimeView(state)
            }
        }
    }
}

@Composable
private fun ProgressView(state: IntervalWidgetState) {
    val progress = when (state.trainingState) {
        TrainingState.Completed -> 0f
        TrainingState.Idle -> 0f
        is TrainingState.Pause -> state.trainingState.progress
        is TrainingState.Running -> state.trainingState.progress
    }
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 300),
        label = "interval-progress"
    )

    val color = when (state.trainingState) {
        TrainingState.Completed -> TODO()
        TrainingState.Idle -> Color.Transparent
        is TrainingState.Pause -> OrangeFill
        is TrainingState.Running -> GreenFill
    }
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(animatedProgress)
            .background(color)
    )
}

@Composable
private fun LeftTimeView(state: IntervalWidgetState) {
    val textColor = when (state.trainingState) {
        TrainingState.Idle -> Color(0xFF5F6776)
        is TrainingState.Running -> Green500
        TrainingState.Completed -> TODO()
        is TrainingState.Pause -> Orange
    }
    Text(
        text = state.left,
        color = textColor,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun RowScope.TitleView(state: IntervalWidgetState) {
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
private fun IconView(state: IntervalWidgetState) {
    val textColor = when (state.trainingState) {
        TrainingState.Idle -> Color(0xFF939BAA)
        is TrainingState.Running -> Color.White
        TrainingState.Completed -> TODO()
        is TrainingState.Pause -> Color.White
    }

    val bgdColor = when (state.trainingState) {
        TrainingState.Idle -> Color(0xFFEDEDEF)
        is TrainingState.Running -> Green500
        TrainingState.Completed -> TODO()
        is TrainingState.Pause -> Orange
    }
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(bgdColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = state.order,
            color = textColor,
            fontSize = 14.sp,
            maxLines = 1,
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

            IntervalWidget(
                state = IntervalWidgetState(
                    trainingState = TrainingState.Running(0.5f),
                    order = "1",
                    title = "Ходьба в среднем темпе",
                    left = "2:25"
                )
            )

            IntervalWidget(
                state = IntervalWidgetState(
                    trainingState = TrainingState.Pause(0.5f),
                    order = "1",
                    title = "Ходьба в среднем темпе",
                    left = "2:25"
                )
            )
//            IntervalItem(index = 1, title = "Ходьба в среднем темпе", timeSeconds = 300)
//            IntervalItem(index = 2, title = "Ходьба в интенсивном темпе", timeSeconds = 300)
//            IntervalItem(index = 3, title = "Ходьба в среднем темпе", timeSeconds = 120)
//            IntervalItem(index = 4, title = "Медленный бег", timeSeconds = 30)
        }
    }
}

private fun IntervalWidgetState.borderColor(): Color = when(trainingState){
    TrainingState.Completed -> TODO()
    TrainingState.Idle -> GreenFill
    is TrainingState.Pause -> OrangeFill
    is TrainingState.Running -> GreenFill
}
