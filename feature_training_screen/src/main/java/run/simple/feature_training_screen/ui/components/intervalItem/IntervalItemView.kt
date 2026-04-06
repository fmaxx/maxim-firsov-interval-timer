package run.simple.feature_training_screen.ui.components.intervalItem

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Green500 = Color(0xFF4CAF50)
private val BorderGray = Color(0xFFE5E7EB)
private val TextGray = Color(0xFF939BAA)
private val GreenFill = Color(0xFFBFE4C4)
private val Orange = Color(0xFFE8883A)
private val OrangeFill = Color(0xFFFBE9D7)
private val Black = Color(0xFF1A1D24)

@Composable
fun IntervalItemView(
    state: IntervalItemState,
    modifier: Modifier = Modifier,
) {

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = BorderStroke(1.dp, state.borderColor()),
        shadowElevation = if (state.trainingState == TrainingState.Completed) 0.dp else 2.dp
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
private fun ProgressView(state: IntervalItemState) {
    val progress = when (state.trainingState) {
        TrainingState.Idle -> 0f
        is TrainingState.Running -> state.trainingState.progress
        is TrainingState.Pause -> state.trainingState.progress
        TrainingState.Completed -> 0f
    }
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 300),
        label = "interval-progress"
    )

    val color = when (state.trainingState) {
        TrainingState.Idle -> Color.Transparent
        is TrainingState.Running -> GreenFill
        is TrainingState.Pause -> OrangeFill
        TrainingState.Completed -> Color(0xFFF5F5F7)
    }
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(animatedProgress)
            .background(color)
    )
}

@Composable
private fun LeftTimeView(state: IntervalItemState) {
    val textColor = when (state.trainingState) {
        TrainingState.Idle -> Color(0xFF5F6776)
        is TrainingState.Running -> Green500
        TrainingState.Completed -> TextGray
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
private fun RowScope.TitleView(state: IntervalItemState) {
    val textDecoration = when (state.trainingState) {
        TrainingState.Completed -> TextDecoration.LineThrough
        TrainingState.Idle -> TextDecoration.None
        is TrainingState.Pause -> TextDecoration.None
        is TrainingState.Running -> TextDecoration.None
    }
    val color = when (state.trainingState) {
        TrainingState.Idle -> Black
        is TrainingState.Running -> Black
        is TrainingState.Pause -> Black
        TrainingState.Completed -> TextGray
    }
    Text(
        text = state.title,
        textDecoration = textDecoration,
        color = color,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.weight(1f)
    )
}

@Composable
private fun IconView(state: IntervalItemState) {
    val textColor = when (state.trainingState) {
        TrainingState.Idle -> Color(0xFF939BAA)
        is TrainingState.Running -> Color.White
        TrainingState.Completed -> Color.White
        is TrainingState.Pause -> Color.White
    }

    val bgdColor = when (state.trainingState) {
        TrainingState.Idle -> Color(0xFFEDEDEF)
        is TrainingState.Running -> Green500
        TrainingState.Completed -> Color.Transparent
        is TrainingState.Pause -> Orange
    }
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(bgdColor),
        contentAlignment = Alignment.Center
    ) {
        if (state.trainingState is TrainingState.Completed) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color(0xFF3B82F6),
                modifier = Modifier.size(18.dp)
            )
        } else {
            Text(
                text = state.order,
                color = textColor,
                fontSize = 14.sp,
                maxLines = 1,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F5F5, widthDp = 360)
@Composable
private fun IntervalItemViewPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            IntervalItemView(
                state = IntervalItemState(
                    trainingState = TrainingState.Idle,
                    order = "1",
                    title = "Ходьба в среднем темпе",
                    left = "5:00"
                )
            )

            IntervalItemView(
                state = IntervalItemState(
                    trainingState = TrainingState.Running(0.5f),
                    order = "1",
                    title = "Ходьба в среднем темпе",
                    left = "2:25"
                )
            )

            IntervalItemView(
                state = IntervalItemState(
                    trainingState = TrainingState.Pause(0.5f),
                    order = "1",
                    title = "Ходьба в среднем темпе",
                    left = "2:25"
                )
            )

            IntervalItemView(
                state = IntervalItemState(
                    trainingState = TrainingState.Completed,
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

private fun IntervalItemState.borderColor(): Color = when (trainingState) {
    TrainingState.Completed -> Color.Transparent
    TrainingState.Idle -> Green500
    is TrainingState.Pause -> Orange
    is TrainingState.Running -> Green500
}
