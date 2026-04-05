package run.simple.feature_loading_screen.ui

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue

@Immutable
data class LoadingUiState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val trainingInput: TextFieldValue = TextFieldValue(
        text = DEFAULT_ID,
        selection = TextRange(DEFAULT_ID.length),
    ),
) {

    val trainingId: Int = trainingInput.text.toIntOrNull() ?: -1
    val isInputValid: Boolean = trainingInput.text.toIntOrNull() != null

    private companion object {

        const val DEFAULT_ID = "68"
    }
}


