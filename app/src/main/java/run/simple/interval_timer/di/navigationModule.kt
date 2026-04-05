package run.simple.interval_timer.di

import androidx.navigation3.runtime.entryProvider
import org.koin.dsl.module
import run.simple.core.navigation.LoadingRoute
import run.simple.core.navigation.Navigator
import run.simple.feature_loading_screen.navigation.featureLoadingScreenNavigation
import run.simple.feature_training_screen.navigation.featureTrainingScreenNavigation
import run.simple.interval_timer.navigation.NavigatorImpl

val navigationModule = module {
    single<Navigator> {
        NavigatorImpl(
            startDestination = LoadingRoute,
            entryProvider = entryProvider {
                featureLoadingScreenNavigation()
                featureTrainingScreenNavigation()
            }
        )
    }
}