package run.simple.feature_loading_screen.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import run.simple.feature_loading_screen.ui.LoadingViewModel

val featureLoadingScreenModule = module {
    viewModelOf(::LoadingViewModel)
}