package run.simple.feature_loading_screen.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import run.simple.feature_loading_screen.ui.LoadingScreen

@Serializable
object LoadingRoute : NavKey

fun EntryProviderScope<NavKey>.featureLoadingScreenNavigation() {
    entry<LoadingRoute> {
        LoadingScreen()
    }
}