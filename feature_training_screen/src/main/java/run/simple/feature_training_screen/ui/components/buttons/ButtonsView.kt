package run.simple.feature_training_screen.ui.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import run.simple.core.theme.DemoColors.Black
import run.simple.core.theme.DemoColors.Blue
import run.simple.core.theme.DemoColors.BorderGray
import run.simple.core.theme.DemoColors.Green500
import run.simple.core.theme.DemoColors.Orange
import run.simple.core.theme.DemoColors.RedError
import run.simple.feature_training_screen.ui.TrainingUiAction
import run.simple.feature_training_screen.ui.components.intervalItem.TrainingState

@Immutable
data class ButtonsState(
    val trainingState: TrainingState,
) {

    companion object {

        val default = ButtonsState(trainingState = TrainingState.Idle)
    }
}

@Composable
fun ButtonsView(
    state: ButtonsState,
    onAction: (TrainingUiAction) -> Unit = {},
) {
    when (state.trainingState) {
        TrainingState.Idle -> {
            PlayButton {
                onAction.invoke(TrainingUiAction.OnStartClick)
            }
        }

        is TrainingState.Running -> {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                PauseButton {
                    onAction.invoke(TrainingUiAction.OnPauseClick)
                }

                Spacer(modifier = Modifier.height(8.dp))

                ResetTrainingButton {
                    onAction.invoke(TrainingUiAction.OnResetClick)
                }
            }
        }

        is TrainingState.Pause -> {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                ResumeButton {
                    onAction.invoke(TrainingUiAction.OnResumeClick)
                }

                Spacer(modifier = Modifier.height(8.dp))

                ResetTrainingButton {
                    onAction.invoke(TrainingUiAction.OnResetClick)
                }
            }
        }

        TrainingState.Completed -> {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                RestartButton {
                    onAction.invoke(TrainingUiAction.OnResetClick)
                }

                Spacer(modifier = Modifier.height(8.dp))

                NewTrainingButton {
                    onAction.invoke(TrainingUiAction.OnNewClick)
                }
            }
        }
    }
}

@Composable
private fun PlayButton(modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    ButtonWithIcon(
        icon = Icons.Filled.PlayArrow,
        modifier = modifier,
        text = "Старт",
        containerColor = Green500,
        onClick = onClick,
    )
}

@Composable
private fun PauseButton(modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    ButtonWithIcon(
        icon = Icons.Filled.Pause,
        modifier = modifier,
        text = "Пауза",
        containerColor = Orange,
        onClick = onClick,
    )
}

@Composable
private fun ResumeButton(modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    ButtonWithIcon(
        icon = Icons.Filled.PlayArrow,
        modifier = modifier,
        text = "Продолжить",
        containerColor = Green500,
        onClick = onClick,
    )
}

@Composable
private fun RestartButton(modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    ButtonWithIcon(
        icon = Icons.Filled.RestartAlt,
        modifier = modifier,
        text = "Запустить заново",
        containerColor = Blue,
        onClick = onClick,
    )
}

@Composable
private fun ButtonWithIcon(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    text: String = "Продолжить",
    containerColor: Color,
    onClick: () -> Unit = {},
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = containerColor),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = text,
            color = Color.White,
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun ResetTrainingButton(onClick: () -> Unit) {
    TrainingButton(
        text = "Сбросить тренировку",
        textColor = RedError,
        borderColor = RedError,
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
    )
}

@Composable
private fun NewTrainingButton(onClick: () -> Unit) {
    TrainingButton(
        text = "Новая тренировка",
        textColor = Black,
        borderColor = BorderGray,
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
    )
}

@Composable
private fun TrainingButton(
    text: String,
    textColor: Color,
    borderColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(width = 1.dp, color = borderColor),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.PlayArrow,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = text,
            color = textColor,
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F5F5, widthDp = 360)
@Composable
private fun Preview() {
    MaterialTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            ButtonsView(
                state = ButtonsState(
                    trainingState = TrainingState.Idle
                )
            )

            ButtonsView(
                state = ButtonsState(
                    trainingState = TrainingState.Running(progress = 0f)
                )
            )

            ButtonsView(
                state = ButtonsState(
                    trainingState = TrainingState.Pause(progress = 0f)
                )
            )

            ButtonsView(
                state = ButtonsState(
                    trainingState = TrainingState.Completed
                )
            )
        }
    }
}
