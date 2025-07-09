package kolsa.test.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kolsa.test.core.common.ApiResult
import kolsa.test.core.common.asApiResult
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

    // пагинация высосана из пальца, но другой причиные нет тянуть и реализовывать БД
    // делаю пагинацию и БД реализацию, чтобы проект выглядел более полноценным
    @OptIn(ExperimentalPagingApi::class)
    fun getTrainings(): Flow<PagingData<TrainingEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            remoteMediator = TrainingRemoteMediator(
                trainingAPI = trainingAPI,
                trainingDatabase = trainingDatabase
            ),
            pagingSourceFactory = { trainingDao.pagingSource() }
        ).flow

    }

    fun getVideoById(id: Int): Flow<ApiResult<VideoResponse>> {
        return flow {
            val response = trainingAPI.getVideoById(id)
            emit(response)
        }
            .flowOn(Dispatchers.IO)
            .asApiResult()
    }

}