package brewary


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BoilVolume(
    @SerialName("unit")
    val unit: String,
    @SerialName("value")
    val value: Int
)