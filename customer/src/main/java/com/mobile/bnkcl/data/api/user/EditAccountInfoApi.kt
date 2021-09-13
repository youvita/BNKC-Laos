package com.mobile.bnkcl.data.api.user

import com.mobile.bnkcl.data.response.user.EditAccountInfoData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PUT

interface EditAccountInfoApi {

    @PUT("api/ca/user/profile")
    suspend fun editAccountInfo(@Body editAccountInfoData: EditAccountInfoData): Response<Boolean>

}