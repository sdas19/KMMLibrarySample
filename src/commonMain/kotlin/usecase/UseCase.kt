package usecase

import ResultHandler
import brewary.BrewaryResponseItem

interface UseCase {
    suspend fun fetchRecipe(): ResultHandler<List<BrewaryResponseItem>>
}