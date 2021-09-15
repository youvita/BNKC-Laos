package com.mobile.bnkcl.data.api.auth

import com.mobile.bnkcl.data.request.auth.LoginRequest
import com.mobile.bnkcl.data.request.auth.LoginRequestNoAuth
import com.mobile.bnkcl.data.request.auth.PreLoginRequest
import com.mobile.bnkcl.data.request.auth.SignUpRequest
import com.mobile.bnkcl.data.request.signup.PreSignUpRequest
import com.mobile.bnkcl.data.response.auth.IdNumRes
import com.mobile.bnkcl.data.response.auth.LoginResponse
import com.mobile.bnkcl.data.response.auth.PreLoginResponse
import com.mobile.bnkcl.data.response.signup.PreSignUpResponse
import com.mobile.bnkcl.data.response.signup.SignUpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthAPI {

    @POST("api/ca/auth/pre-login")
    suspend fun preLogin(@Body preLoginRequest: PreLoginRequest): Response<PreLoginResponse>

    @POST("api/ca/auth/pre-signup")
    suspend fun preSignUp(@Body preSignUpRequest: PreSignUpRequest): Response<PreSignUpResponse>

    @POST("api/ca/auth/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("api/ca/auth/signup")
    suspend fun signUpUser(@Body signUpRequest: SignUpRequest): Response<SignUpResponse>

    @POST("api/ca/auth/login")
    suspend fun loginUserNoAuth(@Body loginRequest: LoginRequestNoAuth): Response<LoginResponse>

    @GET("api/ca/auth/verify-identification-number")
    suspend fun verifyIdentification(
        @Query("username") username: String?,
        @Query("identification_number") identification_number: String
    ): Response<IdNumRes>
}