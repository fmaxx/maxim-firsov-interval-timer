package run.simple.feature_loading_screen.ui

import androidx.compose.ui.text.input.TextFieldValue

sealed interface LoadingUiAction {
    data object OnLoadClick : LoadingUiAction
    data class OnInputChanged(val value: TextFieldValue) : LoadingUiAction
}
