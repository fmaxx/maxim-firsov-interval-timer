package run.simple.repository_api

import run.simple.repository_api.data.TrainingResponse

interface TrainingRepository {

    suspend fun fetchTraining(id: Int): FetchTrainingResult
}

sealed interface FetchTrainingResult {
    data class Success(val response: TrainingResponse) : FetchTrainingResult
    data class Fail(val throwable: Throwable) : FetchTrainingResult
}