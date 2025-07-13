package kolsa.test.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kolsa.test.core.common.ApiResult
import kolsa.test.core.common.asApiResult
import kolsa.test.core.constants.ApiConstants
import kolsa.test.data.local.database.TrainingDao
import kolsa.test.data.local.database.TrainingDatabase
import kolsa.test.data.local.database.TrainingEntity
import kolsa.test.data.network.TrainingAPI
import kolsa.test.data.network.responses.VideoResponse
import kolsa.test.data.paging.TrainingRemoteMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class Repository @Inject constructor(
    private val trainingAPI: TrainingAPI,
    private val trainingDao: TrainingDao,
    private val trainingDatabase: TrainingDatabase
) {

    @OptIn(ExperimentalPagingApi::class)
    fun getTrainings(query: String, type: Int?): Flow<PagingData<TrainingEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            remoteMediator = TrainingRemoteMediator(
                trainingAPI = trainingAPI,
                trainingDatabase = trainingDatabase
            ),
            pagingSourceFactory = { trainingDao.pagingSource(query, type) }
        ).flow

    }

    fun getVideoById(id: Int): Flow<ApiResult<VideoResponse>> {
        return flow {
            val response = trainingAPI.getVideoById(id)
            val result = VideoResponse(
                id = response.id,
                duration = response.duration,
                link = ApiConstants.BASE_URL + response.link.drop(1)
            )
            emit(result)
        }
            .flowOn(Dispatchers.IO)
            .asApiResult()
    }

    fun getTrainingDataById(id: Int): Flow<ApiResult<TrainingEntity?>> {
        return trainingDao.getVideoInfoById(id).asApiResult()
    }

}