package run.simple.repository_api

import run.simple.repository_api.data.TimerResponse

interface TrainingRepository {

    suspend fun fetchTraining(id: Int): FetchTrainingResult
}

sealed interface FetchTrainingResult {
    data class Success(val response: TimerResponse) : FetchTrainingResult
    data class Fail(val throwable: Throwable) : FetchTrainingResult
}