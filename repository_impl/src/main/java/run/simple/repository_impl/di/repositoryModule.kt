package run.simple.repository_impl.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import run.simple.repository_api.TrainingRepository
import run.simple.repository_impl.TrainingRepositoryImpl

val repositoryModule = module {
    singleOf(::TrainingRepositoryImpl) bind TrainingRepository::class
}