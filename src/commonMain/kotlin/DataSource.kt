import io.ktor.util.*
import usecase.UseCaseImpl

@KtorExperimentalAPI
class DataSource {

    private val useCase by lazy {
        UseCaseImpl(engine = Engine().provideEngine())
    }

    suspend fun fetchListFromNetwork() = useCase.fetchRecipe()
}