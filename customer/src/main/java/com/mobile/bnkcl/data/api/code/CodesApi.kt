package com.mobile.bnkcl.data.api.code

import com.mobile.bnkcl.data.response.code.CodesData
import com.mobile.bnkcl.data.response.code.CodesResponse
import com.mobile.bnkcl.data.response.code.ProductResponse
import com.mobile.bnkcl.data.response.dashboard.DashboardResponse
import com.mobile.bnkcl.data.response.dashboard.LeaseApplicationResponse
import com.mobile.bnkcl.data.response.user.ProfileData
import com.mobile.bnkcl.data.response.user.SettingData
import retrofit2.Response
import retrofit2.http.*

interface CodesApi {

    @GET("/api/ca/common/codes")
    suspend fun getCodes(@Query("group_id") group_id: String?): Response<CodesResponse>

    @GET("/api/ca/products")
    suspend fun getProductCodes(@Query("category") category: String?): Response<ProductResponse>

    @GET("/api/ca/products")
    suspend fun getProductCodes(@Query("category") category: String?,
                                @Query("parent_id") parent_id: String?): Response<ProductResponse>

}