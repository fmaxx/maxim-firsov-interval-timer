package run.simple.feature_training_screen.di

import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import run.simple.feature_training_screen.domain.TrainingInteractor
import run.simple.feature_training_screen.domain.TrainingMapper
import run.simple.feature_training_screen.ui.TrainingViewModel

val featureTrainingScreenModule = module {
    viewModelOf(::TrainingViewModel)
    singleOf(::TrainingInteractor)
    singleOf(::TrainingMapper)
}
