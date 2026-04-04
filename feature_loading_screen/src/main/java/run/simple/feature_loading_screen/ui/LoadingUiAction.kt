package run.simple.feature_loading_screen.ui

sealed interface LoadingUiAction {
    data object Retry : LoadingUiAction
}
