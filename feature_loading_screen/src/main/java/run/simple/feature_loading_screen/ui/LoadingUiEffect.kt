package run.simple.feature_loading_screen.ui

sealed interface LoadingUiEffect {
    data object NavigateNext : LoadingUiEffect
    data class ShowError(val message: String) : LoadingUiEffect
}
