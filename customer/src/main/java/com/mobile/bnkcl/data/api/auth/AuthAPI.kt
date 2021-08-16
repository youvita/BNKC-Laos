package com.mobile.bnkcl.data.api.auth

import com.mobile.bnkcl.data.request.auth.LoginRequest
import com.mobile.bnkcl.data.request.auth.LoginRequestNoAuth
import com.mobile.bnkcl.data.request.auth.PreLoginRequest
import com.mobile.bnkcl.data.response.auth.LoginResponse
import com.mobile.bnkcl.data.response.auth.PreLoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthAPI {

    @POST("api/ca/auth/pre-login")
    suspend fun preLogin(@Body preLoginRequest: PreLoginRequest): Response<PreLoginResponse>

    @POST("api/ca/auth/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("api/ca/auth/login")
    suspend fun loginUserNoAuth(@Body loginRequest: LoginRequestNoAuth): Response<LoginResponse>
}