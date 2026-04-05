package run.simple.feature_loading_screen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.referentialEqualityPolicy
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import run.simple.feature_loading_screen.ui.components.LoadButton
import run.simple.feature_loading_screen.ui.components.LoadButtonState

private val Green500 = Color(0xFF4CAF50)
private val GrayBackground = Color(0xFFF5F5F7)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadingScreen(viewModel: LoadingViewModel = koinViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    Content(
        state = state,
        onAction = viewModel::onAction
    )
    AlertEffect(alertsFlow = viewModel.effect)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    state: LoadingUiState,
    onAction: (LoadingUiAction) -> Unit = {},
) {
    Scaffold(
        containerColor = GrayBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Green500),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_recent_history),
                    contentDescription = "Timer",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- Заголовок ---
            Text(
                text = "Интервальный\nтаймер",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 34.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(12.dp))

            // --- Подзаголовок ---
            Text(
                text = "Введите ID тренировки для загрузки\nпрограммы интервалов",
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                color = Color.Gray,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- Поле ввода ---
            OutlinedTextField(
                enabled = !state.isLoading,
                value = state.trainingInput,
                onValueChange = { value ->
                    onAction.invoke(LoadingUiAction.OnInputChanged(value))
                },
                label = { Text("ID тренировки") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Green500,
                    unfocusedBorderColor = Color.LightGray,
                    focusedLabelColor = Green500,
                    cursorColor = Green500,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                isError = state.isError,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // --- Кнопка «Загрузить» ---
            val buttonState = when {
                state.isLoading -> LoadButtonState.Loading
                state.isError -> LoadButtonState.Error()
                else -> LoadButtonState.Idle
            }
            LoadButton(
                state = buttonState,
                isEnabled = state.isInputValid,
                onClick = {
                    onAction.invoke(LoadingUiAction.OnLoadClick)
                }
            )
        }
    }
}

@ExperimentalMaterial3Api
@Composable
private fun AlertEffect(
    alertsFlow: Flow<LoadingUiEffect>,
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    var alertState by remember {
        mutableStateOf<LoadingUiEffect?>(
            value = null,
            policy = referentialEqualityPolicy()
        )
    }

    LaunchedEffect(Unit) {
        alertsFlow
            .flowWithLifecycle(
                lifecycle = lifecycleOwner.lifecycle,
                minActiveState = Lifecycle.State.STARTED
            )
            .collect { event ->
                alertState = event
            }
    }

    when (val state = alertState) {
        is LoadingUiEffect.ShowError -> {
            AlertDialog(
                onDismissRequest = {
                    alertState = null
                },
                title = {
                    Text(text = "Ошибка")
                },
                text = {
                    Text(text = state.message)
                },
                confirmButton = {
                    TextButton(onClick = {
                        alertState = null
                    }) {
                        Text(text = "Ок")
                    }
                }
            )
        }

        null -> Unit
    }
}

@Preview
@Composable
private fun LoadingScreenPreview() {
    Content(
        state = LoadingUiState(
            isLoading = false
        )
    )
}