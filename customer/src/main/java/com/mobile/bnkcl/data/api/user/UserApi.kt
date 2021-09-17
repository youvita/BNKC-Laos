package com.mobile.bnkcl.data.api.user

import com.mobile.bnkcl.data.request.auth.LoginRequestNoAuth
import com.mobile.bnkcl.data.request.user.PreChangeRequest
import com.mobile.bnkcl.data.request.user.ResetPasswordRequest
import com.mobile.bnkcl.data.response.auth.LoginResponse
import com.mobile.bnkcl.data.response.user.ProfileData
import com.mobile.bnkcl.data.response.user.SettingData
import com.mobile.bnkcl.data.response.user.resetpassword.PreChangeResponse
import retrofit2.Response
import retrofit2.http.*

interface UserApi {

    @GET("api/ca/user/profile")
    suspend fun getProfile(): Response<ProfileData>

    @PATCH("api/ca/user/setting")
    suspend fun updateUserSetting(@Body settingData: SettingData): Response<Unit>

    @GET("api/ca/auth/logout")
    suspend fun logout(): Response<Unit>

    @POST("/api/ca/user/pre-change-password")
    suspend fun preChangePassword(@Body request: PreChangeRequest): Response<PreChangeResponse>

    @PUT("/api/ca/user/change-password")
    suspend fun resetPassword(@Body resetPasswordRequest: ResetPasswordRequest): Response<Unit>

}