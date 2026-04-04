package run.simple.repository_impl

import run.simple.repository_api.FetchTrainingResult
import run.simple.repository_api.TrainingRepository
import run.simple.repository_impl.api.TrainingApi

class TrainingRepositoryImpl(
    private val trainingApi: TrainingApi,
) : TrainingRepository {

    override suspend fun fetchTraining(id: Int): FetchTrainingResult = try {
        val response = trainingApi.fetchTraining(id)
        FetchTrainingResult.Success(response)
    } catch (e: Exception) {
        FetchTrainingResult.Fail(e)
    }
}