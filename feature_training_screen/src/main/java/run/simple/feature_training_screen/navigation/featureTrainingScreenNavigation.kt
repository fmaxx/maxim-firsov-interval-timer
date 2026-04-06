package run.simple.feature_training_screen.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import run.simple.core.navigation.TrainingRoute
import run.simple.feature_training_screen.ui.TrainingScreen

fun EntryProviderScope<NavKey>.featureTrainingScreenNavigation() =
    entry<TrainingRoute> {
        TrainingScreen(route = it)
    }
