package run.simple.feature_training_screen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.referentialEqualityPolicy
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import run.simple.core.navigation.TrainingRoute
import run.simple.core.theme.DemoColors
import run.simple.feature_training_screen.ui.components.buttons.ButtonsState
import run.simple.feature_training_screen.ui.components.buttons.ButtonsView
import run.simple.feature_training_screen.ui.components.interval.IntervalsView
import run.simple.feature_training_screen.ui.components.intervalItem.IntervalItemState
import run.simple.feature_training_screen.ui.components.intervalItem.TrainingState
import run.simple.feature_training_screen.ui.components.trainingCard.TrainingCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainingScreen(
    route: TrainingRoute,
    viewModel: TrainingViewModel = koinViewModel(),
    onBackClick: () -> Unit = {},
) {
    LaunchedEffect(route) {
        viewModel.setData(route.data)
    }

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
                        text = state.topBarState.title,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = DemoColors.Black,
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
                    val color = when (state.topBarState.trainingState) {
                        TrainingState.Idle -> DemoColors.TextGray
                        is TrainingState.Running -> DemoColors.Green500
                        is TrainingState.Pause -> DemoColors.Orange
                        TrainingState.Completed -> DemoColors.Blue
                    }

                    Text(
                        text = state.topBarState.totaLeftTime,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = color
                    )

                },
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = DemoColors.GrayBackground
                ),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            )
        },
    ) { paddingValues ->
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(DemoColors.GrayBackground)
                .padding(paddingValues),
        ) {

            val (
                buttonsBox,
                trainingCard,
                intervalsView,
            ) = createRefs()

            // CARD
            TrainingCard(
                state = state.trainingCardState,
                modifier = Modifier
                    .padding(all = 16.dp)
                    .constrainAs(trainingCard) {
                        top.linkTo(parent.top)
                    }
            )

            IntervalsView(
                state = state.intervalsViewState,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp, bottom = 8.dp)
                    .constrainAs(intervalsView) {
                        top.linkTo(trainingCard.bottom)
                        bottom.linkTo(buttonsBox.top)
                        height = Dimension.fillToConstraints
                    }
            )

            // BUTTONS
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
                    .constrainAs(buttonsBox) {
                        bottom.linkTo(parent.bottom)
                    }
            ) {
                ButtonsView(state.buttonsState)
            }
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

/*@Preview
@Composable
private fun IdlePreview() {
    Content(
        state = TrainingUiState(
            topBarState = TopbarState(
                trainingState = TrainingState.Idle,
                totaLeftTime = "15:00",
                title = "Tренировка 7",
            ),
            buttonsState = ButtonsState(
                trainingState = TrainingState.Idle
            )
        ),
    )
}*/

@Preview
@Composable
private fun RunningPreview() {
    Content(
        state = TrainingUiState(
            topBarState = TopbarState(
                trainingState = TrainingState.Running(0.5f),
                totaLeftTime = "● 12:18",
                title = "Tренировка 7",
            ),
            buttonsState = ButtonsState(
                trainingState = TrainingState.Running(0.5f)
            ),
            trainingCardState = TrainingCardState(
                trainingState = TrainingState.Running(0.5f),
                status = "ВЫПОЛНЯЕТСЯ",
                trainingName = "Медленный бег",
                currentTimeLeft = "00:18",
                totalProgressMessage = "Прошло 12:18 из 15:00"
            ),
            intervalsViewState = IntervalsViewState(
                items = persistentListOf(
                    IntervalItemState(
                        trainingState = TrainingState.Idle,
                        order = "1",
                        title = "Ходьба в среднем темпе",
                        left = "5:00"
                    ),

                    IntervalItemState(
                        trainingState = TrainingState.Idle,
                        order = "2",
                        title = "Ходьба в среднем темпе",
                        left = "5:00"
                    ),

                    IntervalItemState(
                        trainingState = TrainingState.Idle,
                        order = "3",
                        title = "Ходьба в среднем темпе",
                        left = "5:00"
                    ),

                    IntervalItemState(
                        trainingState = TrainingState.Running(0.5f),
                        order = "5",
                        title = "Ходьба в среднем темпе",
                        left = "5:00"
                    ),

                    IntervalItemState(
                        trainingState = TrainingState.Idle,
                        order = "6",
                        title = "Ходьба в среднем темпе",
                        left = "5:00"
                    ),

                    IntervalItemState(
                        trainingState = TrainingState.Pause(0.75f),
                        order = "7",
                        title = "Ходьба в среднем темпе",
                        left = "5:00"
                    ),

                    IntervalItemState(
                        trainingState = TrainingState.Completed,
                        order = "8",
                        title = "Ходьба в среднем темпе",
                        left = "5:00"
                    ),
                ),
                progress = "7 из 7 ✓"
            )
        ),
    )
}

/*@Preview
@Composable
private fun PausePreview() {
    Content(
        state = TrainingUiState(
            topBarState = TopbarState(
                trainingState = TrainingState.Pause(0.5f),
                totaLeftTime = "❚❚ Пауза",
                title = "Tренировка 7",
            ),
            buttonsState = ButtonsState(
                trainingState = TrainingState.Pause(0.5f)
            )
        ),
    )
}

@Preview
@Composable
private fun CompletePreview() {
    Content(
        state = TrainingUiState(
            topBarState = TopbarState(
                trainingState = TrainingState.Completed,
                totaLeftTime = "Завершена",
                title = "Tренировка 7",
            ),
            buttonsState = ButtonsState(
                trainingState = TrainingState.Completed
            )
        ),
    )
}*/
