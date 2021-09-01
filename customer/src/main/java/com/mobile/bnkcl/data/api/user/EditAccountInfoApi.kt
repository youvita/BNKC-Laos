package com.mobile.bnkcl.data.api.user

import com.mobile.bnkcl.data.response.user.EditAccountInfoData
import retrofit2.Response
import retrofit2.http.*

interface EditAccountInfoApi {

    @PUT("api/ca/user/profile")
    suspend fun isUpdate(@Body updateData: EditAccountInfoData): Response<Boolean>

}