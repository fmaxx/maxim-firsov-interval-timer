package run.simple.feature_loading_screen.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Green500 = Color(0xFF4CAF50)
private val GreenLight = Color(0xFFE8F5E9)
private val GreenOnLight = Color(0xFF2E7D32)
private val ErrorRed = Color(0xFFD32F2F)

/** Состояния кнопки загрузки. */
sealed interface LoadButtonState {

    data object Idle : LoadButtonState
    data object Loading : LoadButtonState
    data class Error(val message: String = "Тренировка не найдена. Проверьте ID.") : LoadButtonState
}

@Composable
fun LoadButton(
    state: LoadButtonState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    idleText: String = "Загрузить",
    loadingText: String = "Загрузка...",
    retryText: String = "Повторить",
) {
    Column(modifier = modifier.fillMaxWidth()) {

        // Сообщение об ошибке — показывается только в состоянии Error.
        if (state is LoadButtonState.Error) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Text(
                    text = state.message,
                    color = ErrorRed,
                    fontSize = 14.sp
                )
            }
        }

        when (state) {
            is LoadButtonState.Idle -> PrimaryButton(
                text = idleText,
                onClick = onClick,
                isEnabled = isEnabled
            )

            is LoadButtonState.Loading -> LoadingButton(text = loadingText)
            is LoadButtonState.Error -> PrimaryButton(
                text = retryText,
                onClick = onClick,
                isEnabled = isEnabled
            )
        }
    }
}

@Composable
private fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    isEnabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = isEnabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Green500),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
    ) {
        Text(
            text = text,
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
    }
}

@Composable
private fun LoadingButton(text: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(12.dp),
        color = GreenLight
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = 0.5.dp,
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFB0B5BF),
                ),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,

            ) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = GreenOnLight,
                strokeWidth = 2.dp
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = text,
                color = GreenOnLight,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// ---------- Previews ----------

@Preview(showBackground = true, backgroundColor = 0xFFF5F5F5, widthDp = 360)
@Composable
private fun LoadButtonIdlePreview() {
    MaterialTheme {
        Box(Modifier.padding(16.dp)) {
            LoadButton(state = LoadButtonState.Idle, onClick = {})
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F5F5, widthDp = 360)
@Composable
private fun LoadButtonLoadingPreview() {
    MaterialTheme {
        Box(Modifier.padding(16.dp)) {
            LoadButton(state = LoadButtonState.Loading, onClick = {})
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F5F5, widthDp = 360)
@Composable
private fun LoadButtonErrorPreview() {
    MaterialTheme {
        Box(Modifier.padding(16.dp)) {
            LoadButton(state = LoadButtonState.Error(), onClick = {})
        }
    }
}