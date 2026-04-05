package run.simple.feature_training_screen.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import run.simple.feature_training_screen.ui.TrainingViewModel

val featureTrainingScreenModule = module {
    viewModelOf(::TrainingViewModel)
}
