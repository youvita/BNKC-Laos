package com.mobile.bnkcl.data.api.area

import com.mobile.bnkcl.data.response.area.AreaRes
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AreaApi {
    @GET("api/ca/areas")
    suspend fun getCapital(@Query("category") category: String): Response<AreaRes>

    @GET("api/ca/areas")
    suspend fun getAddress(@Query("category") category: String,
                           @Query("parent_id") parent_id: String
    ): Response<AreaRes>


}