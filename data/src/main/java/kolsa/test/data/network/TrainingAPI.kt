package kolsa.test.data.network

import kolsa.test.core.constants.ApiConstants
import kolsa.test.data.network.responses.VideoResponse
import kolsa.test.data.network.responses.TrainingItem
import retrofit2.http.GET
import retrofit2.http.Query


interface TrainingAPI {

    @GET(ApiConstants.GET_WORKOUT_LIST)
    suspend fun getTrainingList() : List<TrainingItem>

    @GET(ApiConstants.GET_WORKOUT_VIDEO)
    suspend fun getVideoById(
        @Query("id") id: Int
    ) : VideoResponse

}