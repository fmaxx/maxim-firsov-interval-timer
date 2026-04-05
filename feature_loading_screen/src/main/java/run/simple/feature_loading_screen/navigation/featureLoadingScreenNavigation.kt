package run.simple.feature_loading_screen.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import run.simple.core.navigation.LoadingRoute
import run.simple.feature_loading_screen.ui.LoadingScreen

fun EntryProviderScope<NavKey>.featureLoadingScreenNavigation() {
    entry<LoadingRoute> {
        LoadingScreen()
    }
}