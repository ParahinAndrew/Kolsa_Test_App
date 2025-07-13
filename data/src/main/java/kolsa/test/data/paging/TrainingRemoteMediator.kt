package kolsa.test.data.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import kolsa.test.data.local.database.TrainingDatabase
import kolsa.test.data.local.database.TrainingEntity
import kolsa.test.data.network.TrainingAPI
import kolsa.test.data.network.responses.TrainingItem
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class TrainingRemoteMediator @Inject constructor(
    private val trainingAPI: TrainingAPI,
    private val trainingDatabase: TrainingDatabase
) : RemoteMediator<Int, TrainingEntity>() {

    private val trainingDao = trainingDatabase.trainingDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, TrainingEntity>
    ): MediatorResult {
        return try {
            if (loadType == LoadType.REFRESH) {
                val response = trainingAPI.getTrainingList()
                val entities = response.map { it.toEntity() }

                trainingDatabase.withTransaction {
                    trainingDao.clearAll()
                    trainingDao.insertAll(entities)
                }
            }
            MediatorResult.Success(endOfPaginationReached = loadType != LoadType.REFRESH)
        } catch (e: IOException) {
            Log.e("RemoteMediator", "IOException", e)
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            Log.e("RemoteMediator", "HttpException", e)
            MediatorResult.Error(e)
        } catch (e: Exception) {
            Log.e("RemoteMediator", "Generic Exception", e)
            MediatorResult.Error(e)
        }
    }

    private fun TrainingItem.toEntity(): TrainingEntity {
        return TrainingEntity(
            id = this.id,
            title = this.title,
            description = this.description,
            type = this.type,
            duration = this.duration
        )
    }
}