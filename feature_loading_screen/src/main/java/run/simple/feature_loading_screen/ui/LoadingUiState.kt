package run.simple.feature_loading_screen.ui

import androidx.compose.runtime.Immutable

@Immutable
data class LoadingUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
)
