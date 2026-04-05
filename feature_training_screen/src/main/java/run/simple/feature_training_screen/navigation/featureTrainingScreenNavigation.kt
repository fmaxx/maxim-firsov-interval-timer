package run.simple.feature_training_screen.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import run.simple.feature_training_screen.ui.TrainingScreen

@Serializable
object TrainingRoute : NavKey

fun EntryProviderScope<NavKey>.featureTrainingScreenNavigation() =
    entry<TrainingRoute> {
        TrainingScreen()
    }
