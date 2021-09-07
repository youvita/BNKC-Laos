package com.mobile.bnkcl.data.api.user

import com.mobile.bnkcl.data.response.user.ProfileData
import com.mobile.bnkcl.data.response.user.SettingData
import retrofit2.Response
import retrofit2.http.*

interface UserApi {

    @GET("api/ca/user/profile")
    suspend fun getProfile(): Response<ProfileData>

    @PATCH("api/ca/user/setting")
    suspend fun updateUserSetting(@Body settingData: SettingData): Response<Unit>

    @GET("api/ca/auth/logout")
    suspend fun logout(): Response<Unit>

}