package run.simple.feature_training_screen.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.referentialEqualityPolicy
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainingScreen(
    viewModel: TrainingViewModel = koinViewModel(),
    onBackClick: () -> Unit = {},
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    Content(
        state = state,
        onAction = viewModel::onAction,
        onBackClick = onBackClick,
    )
    EffectHandler(effectFlow = viewModel.effect)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    state: TrainingUiState,
    onAction: (TrainingUiAction) -> Unit = {},
    onBackClick: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = state.title,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад",
                        )
                    }
                },
                actions = {
                    Text(
                        text = state.totalTime,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                    )
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            /*Text(
                text = state.title.ifEmpty { "Тренировка" },
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = state.currentIntervalTitle.ifEmpty { "—" },
                fontSize = 18.sp,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "${state.currentIntervalTimeLeft} s",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.height(32.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Button(
                    onClick = {
                        if (state.isRunning) {
                            onAction(TrainingUiAction.OnPauseClick)
                        } else {
                            onAction(TrainingUiAction.OnStartClick)
                        }
                    },
                    modifier = Modifier.weight(1f),
                ) {
                    Text(if (state.isRunning) "Пауза" else "Старт")
                }
                Button(
                    onClick = { onAction(TrainingUiAction.OnResetClick) },
                    modifier = Modifier.weight(1f),
                ) {
                    Text("Сброс")
                }
            }*/
        }
    }
}

@Composable
private fun EffectHandler(effectFlow: Flow<TrainingUiEffect>) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var lastEffect by remember {
        mutableStateOf<TrainingUiEffect?>(
            value = null,
            policy = referentialEqualityPolicy(),
        )
    }

    LaunchedEffect(Unit) {
        effectFlow
            .flowWithLifecycle(
                lifecycle = lifecycleOwner.lifecycle,
                minActiveState = Lifecycle.State.STARTED,
            )
            .collect { lastEffect = it }
    }

    when (lastEffect) {
        is TrainingUiEffect.ShowError -> Unit
        TrainingUiEffect.TrainingFinished -> Unit
        null -> Unit
    }
}

@Preview
@Composable
private fun TrainingScreenPreview() {
    Content(
        state = TrainingUiState(
            title = "Утренняя разминка",
            currentIntervalTitle = "Бег",
            currentIntervalTimeLeft = 42,
            isRunning = true,
        ),
    )
}
