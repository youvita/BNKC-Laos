package com.mobile.bnkcl.data.api

import com.mobile.bnkcl.data.response.user.ProfileData
import retrofit2.Response
import retrofit2.http.GET

interface UserApi {

    @GET("api/ca/user/profile")
    suspend fun getProfile(): Response<ProfileData>

}