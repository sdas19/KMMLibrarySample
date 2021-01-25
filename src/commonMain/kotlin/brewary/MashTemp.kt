package brewary


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MashTemp(
    @SerialName("duration")
    val duration: Int?,
    @SerialName("temp")
    val temp: TempX
)