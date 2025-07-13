package kolsa.test.feature_training.domain

import androidx.paging.PagingData
import kolsa.test.core.common.ApiResult
import kolsa.test.data.Repository
import kolsa.test.data.local.database.TrainingEntity
import kolsa.test.data.network.responses.VideoResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TrainingsUseCase @Inject constructor(
    private val repository: Repository
) {

    operator fun invoke(query: String, type: Int?): Flow<PagingData<TrainingEntity>> {
        return repository.getTrainings(query, type)
    }

    // Не вижу смысла выносить методы в отдельный use_case. Не знаю как у вас принято)
    fun getVideoLinkById(id: Int): Flow<ApiResult<VideoResponse>> {
        return repository.getVideoById(id)
    }

    fun getVideoInfoById(id: Int): Flow<ApiResult<TrainingEntity?>> {
        return repository.getTrainingDataById(id)
    }

}