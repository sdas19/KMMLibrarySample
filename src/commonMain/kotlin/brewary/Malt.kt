package brewary


import brewary.AmountX
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Malt(
    @SerialName("amount")
    val amount: AmountX,
    @SerialName("name")
    val name: String
)