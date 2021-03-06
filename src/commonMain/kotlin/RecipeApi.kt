import brewary.BrewaryResponseItem
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.http.*

class RecipeApi(private val engine: HttpClientEngine) {

    private val client by lazy {
        HttpClient(engine) {
            install(JsonFeature) {
                accept(ContentType.Application.Json)
                serializer = KotlinxSerializer( kotlinx.serialization.json.Json {
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    suspend fun fetchRecipe(): ResultHandler<List<BrewaryResponseItem>> {
        return runInDifferentCoroutine {
            client.get {
                url("$baseUrl/v2/beers")
            }
        }
    }

    companion object {
        private const val baseUrl = "https://api.punkapi.com"
    }
}