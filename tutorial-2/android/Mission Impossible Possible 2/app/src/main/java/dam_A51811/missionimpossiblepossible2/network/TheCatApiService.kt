package dam_A51811.missionimpossiblepossible2.network

import dam_A51811.missionimpossiblepossible2.model.CatImageItem
import retrofit2.http.GET
import retrofit2.http.Query

interface TheCatApiService {

    @GET("v1/images/search")
    suspend fun searchImages(
        @Query("limit") limit: Int = 10
    ): List<CatImageItem>
}
