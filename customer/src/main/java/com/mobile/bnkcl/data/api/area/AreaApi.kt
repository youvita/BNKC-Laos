package com.mobile.bnkcl.data.api.area

import com.mobile.bnkcl.data.response.area.CapitalData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AreaApi {
    @GET("api/ca/areas")
    suspend fun getCapital(@Query("category") category: String): Response<CapitalData>
}