package brewary


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AmountX(
    @SerialName("unit")
    val unit: String,
    @SerialName("value")
    val value: Double
)