package brewary


import brewary.Amount
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Hop(
    @SerialName("add")
    val add: String,
    @SerialName("amount")
    val amount: Amount,
    @SerialName("attribute")
    val attribute: String,
    @SerialName("name")
    val name: String
)