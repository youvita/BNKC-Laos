package com.mobile.bnkcl.data.repository.auth

import android.content.Context
import com.bnkc.library.data.network.RemoteDataSource
import com.bnkc.library.data.type.Resource
import com.bnkc.sourcemodule.app.RetrofitBuilder
import com.mobile.bnkcl.data.api.auth.AuthAPI
import com.mobile.bnkcl.data.request.auth.LoginRequest
import com.mobile.bnkcl.data.request.auth.LoginRequestNoAuth
import com.mobile.bnkcl.data.request.auth.PreLoginRequest
import com.mobile.bnkcl.data.response.auth.LoginResponse
import com.mobile.bnkcl.data.response.auth.PreLoginResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import retrofit2.Response

class AuthRepo(context: Context, okHttpClient: OkHttpClient) {

    private val authAPI: AuthAPI by lazy { RetrofitBuilder(context, okHttpClient).getRetrofit().create(AuthAPI::class.java) }

    fun preLogin(preLoginRequest: PreLoginRequest): Flow<Resource<PreLoginResponse>> = flow {
        try {
            delay(1000)
            val request = object : RemoteDataSource<PreLoginResponse>(){
                override suspend fun createCall(): Response<PreLoginResponse> {
                    return authAPI.preLogin(preLoginRequest)
                }
            }
            request.networkRequest()
            emit(request.asLiveData().value!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun loginUser(loginRequest: LoginRequest): Flow<Resource<LoginResponse>> = flow {
        try {
            delay(1000)
            val request = object : RemoteDataSource<LoginResponse>() {
                override suspend fun createCall(): Response<LoginResponse> {
                    return authAPI.loginUser(loginRequest)
                }
            }
            request.networkRequest()
            emit(request.asLiveData().value!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun loginUserNoAuth(loginRequest: LoginRequestNoAuth): Flow<Resource<LoginResponse>> = flow {
        try {
            delay(1000)
            val request = object : RemoteDataSource<LoginResponse>() {
                override suspend fun createCall(): Response<LoginResponse> {
                    return authAPI.loginUserNoAuth(loginRequest)
                }
            }
            request.networkRequest()
            emit(request.asLiveData().value!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}