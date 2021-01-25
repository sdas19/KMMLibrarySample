package brewary


import brewary.Fermentation
import brewary.MashTemp
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Method(
    @SerialName("fermentation")
    val fermentation: Fermentation,
    @SerialName("mash_temp")
    val mashTemp: List<MashTemp>,
    @SerialName("twist")
    val twist: String?
)