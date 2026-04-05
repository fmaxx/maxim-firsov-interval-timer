package run.simple.feature_loading_screen.ui

sealed interface LoadingUiEffect {
    data class ShowError(val message: String) : LoadingUiEffect
}
