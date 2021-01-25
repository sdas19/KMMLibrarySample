package usecase

import RecipeApi
import ResultHandler
import brewary.BrewaryResponseItem
import io.ktor.client.engine.*

class UseCaseImpl(private val engine: HttpClientEngine) : UseCase {

    override suspend fun fetchRecipe(): ResultHandler<List<BrewaryResponseItem>> {
        return RecipeApi(engine).fetchRecipe()
    }
}