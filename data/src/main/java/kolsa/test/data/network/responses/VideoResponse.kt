package kolsa.test.data.network.responses

import com.google.gson.annotations.SerializedName

data class VideoResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("duration") val duration: Int,
    @SerializedName("link") val link: String
)