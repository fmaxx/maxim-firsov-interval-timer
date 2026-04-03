package run.simple.repository_impl

import run.simple.repository_api.FetchTrainingResult
import run.simple.repository_api.TrainingRepository

class TrainingRepositoryImpl: TrainingRepository {

    override suspend fun fetchTraining(id: Int): FetchTrainingResult {
        TODO("Not yet implemented")
    }
}