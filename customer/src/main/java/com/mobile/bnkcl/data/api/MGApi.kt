package com.mobile.bnkcl.data.api

import com.mobile.bnkcl.app.AppBuild
import com.mobile.bnkcl.data.MGData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MGApi {
    @GET("api/${AppBuild.MGVersion.MG_MOBILE_VERSION_2}/app/setting/18537b59-18fa-4b5a-8a27-49905c9e3337")
    suspend fun getMGData(@Query("os") os: String): Response<MGData>

}