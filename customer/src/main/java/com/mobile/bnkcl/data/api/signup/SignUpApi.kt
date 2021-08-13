package com.mobile.bnkcl.data.api.signup

import com.mobile.bnkcl.data.PostItem
import com.mobile.bnkcl.data.request.signup.PreSignUpRequest
import com.mobile.bnkcl.data.response.signup.PreSignUpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SignUpApi {

    @POST("api/ca/auth/pre-signup")
    suspend fun setPreSignUpRequest(@Body preSignUpRequest: PreSignUpRequest) : Response<PreSignUpResponse>
}