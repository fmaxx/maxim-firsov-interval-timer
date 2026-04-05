package run.simple.interval_timer.di

import org.koin.dsl.module
import run.simple.feature_loading_screen.di.featureLoadingScreenModule
import run.simple.feature_training_screen.di.featureTrainingScreenModule
import run.simple.repository_impl.di.repositoryModule

val appModule = module {
    includes(
        navigationModule,
        repositoryModule,
        featureLoadingScreenModule,
        featureTrainingScreenModule,
    )
}