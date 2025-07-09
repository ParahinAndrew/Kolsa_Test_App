package kolsa.test.data.network.responses

import com.google.gson.annotations.SerializedName

data class TrainingItem(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("type") val type: Int?,
    @SerializedName("duration") val duration: String?
)