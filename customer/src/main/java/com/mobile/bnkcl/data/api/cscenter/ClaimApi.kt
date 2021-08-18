package com.mobile.bnkcl.data.api.cscenter

import com.mobile.bnkcl.data.response.cscenter.ClaimDataResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ClaimApi {

    @GET("api/ca/claims")
    suspend fun getClaimData(
                          @Query("page_number") page_number: Int?,
                          @Query("page_size") page_size: Int?,
                          @Query("sort_by") sort_by: String?) : Response<ClaimDataResponse>
}