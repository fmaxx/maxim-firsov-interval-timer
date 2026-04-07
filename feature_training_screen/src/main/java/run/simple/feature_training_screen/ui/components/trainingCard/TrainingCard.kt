package run.simple.feature_training_screen.ui.components.trainingCard

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import run.simple.core.theme.DemoColors
import run.simple.feature_training_screen.ui.TrainingCardState
import run.simple.feature_training_screen.ui.components.intervalItem.TrainingState

@Composable
fun TrainingCard(
    state: TrainingCardState,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(24.dp),
                clip = false
            ),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(width = 1.dp, color = state.borderColor()),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(state.gradientColors())
                )
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- Статус сверху ---
            StatusText(state)

            Spacer(Modifier.height(6.dp))

            // --- Название текущего интервала ---
            IntervalText(state)

            Spacer(Modifier.height(12.dp))

            // --- Большое время ---
            TimeText(state)

            Spacer(Modifier.height(8.dp))

            // --- Подпись под временем ---
            DescriptionText(state)

            Spacer(Modifier.height(16.dp))

            // --- Прогресс-бар ---
            TrainingProgress(state)

            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
private fun TrainingProgress(state: TrainingCardState) {
    val animatedProgress by animateFloatAsState(
        targetValue = state.totalProgress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 300),
        label = "timer-progress"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .clip(RoundedCornerShape(2.dp))
            .background(DemoColors.BorderGray)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(animatedProgress)
                .background(state.progressColor())
        )
    }
}

@Composable
private fun DescriptionText(state: TrainingCardState) {
    Text(
        text = state.totalProgressMessage,
        color = DemoColors.TextGray,
        fontSize = 14.sp,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
private fun TimeText(state: TrainingCardState) {
    Text(
        text = state.currentTimeLeft,
        color = DemoColors.Black,
        fontSize = 72.sp,
        fontWeight = FontWeight.Black,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun IntervalText(state: TrainingCardState) {
    Text(
        text = state.trainingName,
        color = state.titleColor(),
        fontSize = 17.sp,
        fontWeight = FontWeight.Medium,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun StatusText(state: TrainingCardState) {
    Text(
        text = state.status,
        color = state.statusColor(),
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 1.5.sp,
        textAlign = TextAlign.Center
    )
}

private fun TrainingCardState.borderColor(): Color = when (trainingState) {
    TrainingState.Idle -> DemoColors.BorderGray
    is TrainingState.Running -> DemoColors.Green500
    is TrainingState.Pause -> DemoColors.Orange
    TrainingState.Completed -> DemoColors.Blue
}

private fun TrainingCardState.statusColor(): Color = when (trainingState) {
    TrainingState.Idle -> DemoColors.TextGray
    is TrainingState.Running -> DemoColors.Green500
    is TrainingState.Pause -> DemoColors.Orange
    TrainingState.Completed -> DemoColors.Blue
}

private fun TrainingCardState.titleColor(): Color = when (trainingState) {
    TrainingState.Idle -> DemoColors.Black
    is TrainingState.Running -> DemoColors.Black
    is TrainingState.Pause -> DemoColors.Orange
    TrainingState.Completed -> DemoColors.Blue
}

private fun TrainingCardState.progressColor(): Color = when (trainingState) {
    TrainingState.Idle -> Color.Transparent
    is TrainingState.Running -> DemoColors.Green500
    is TrainingState.Pause -> DemoColors.Orange
    TrainingState.Completed -> DemoColors.Blue
}

private fun TrainingCardState.gradientColors(): List<Color> = when (trainingState) {
    TrainingState.Idle -> listOf(Color.Transparent, Color.Transparent)
    is TrainingState.Running -> listOf(DemoColors.Green500.copy(alpha = 0.05f), Color.Transparent)
    is TrainingState.Pause -> listOf(DemoColors.Orange.copy(alpha = 0.05f), Color.Transparent)
    TrainingState.Completed -> listOf(DemoColors.Blue.copy(alpha = 0.05f), Color.Transparent)
}

// ---------- Preview ----------

@Preview(showBackground = true, backgroundColor = 0xFFF0F1F3, widthDp = 360, heightDp = 320)
@Composable
private fun IdlePreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            TrainingCard(
                state = TrainingCardState(
                    trainingState = TrainingState.Idle,
                    status = "ГОТОВО К СТАРТУ",
                    trainingName = "Ходьба в среднем темпе",
                    currentTimeLeft = "15:00",
                    totalProgressMessage = "Общее время"
                )
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0F1F3, widthDp = 360, heightDp = 320)
@Composable
private fun RunningPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            TrainingCard(
                state = TrainingCardState(
                    trainingState = TrainingState.Running(0.5f),
                    status = "ВЫПОЛНЯЕТСЯ",
                    trainingName = "Медленный бег",
                    currentTimeLeft = "00:18",
                    totalProgressMessage = "Прошло 12:18 из 15:00"
                )
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0F1F3, widthDp = 360, heightDp = 320)
@Composable
private fun PausedPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            TrainingCard(
                state = TrainingCardState(
                    trainingState = TrainingState.Pause(0.5f),
                    status = "НА ПАУЗЕ",
                    trainingName = "Медленный бег",
                    currentTimeLeft = "00:18",
                    totalProgressMessage = "Прошло 12:18 из 15:00"
                )
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0F1F3, widthDp = 360, heightDp = 320)
@Composable
private fun CompletedPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            TrainingCard(
                state = TrainingCardState(
                    trainingState = TrainingState.Completed,
                    status = "ТРЕНИРОВКА ЗАВЕРШЕНА",
                    trainingName = "Отличная работа!",
                    currentTimeLeft = "00:00",
                    totalProgressMessage = "15:00 из 15:00"
                )
            )
        }
    }
}