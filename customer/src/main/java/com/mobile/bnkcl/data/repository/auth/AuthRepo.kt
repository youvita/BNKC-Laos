package com.mobile.bnkcl.data.repository.auth

import com.bnkc.library.data.network.RemoteDataSource
import com.bnkc.library.data.type.Resource
import com.mobile.bnkcl.data.api.auth.AuthAPI
import com.mobile.bnkcl.data.request.auth.LoginRequest
import com.mobile.bnkcl.data.request.auth.LoginRequestNoAuth
import com.mobile.bnkcl.data.request.auth.PreLoginRequest
import com.mobile.bnkcl.data.response.auth.LoginResponse
import com.mobile.bnkcl.data.response.auth.PreLoginResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class AuthRepo(private val authAPI: AuthAPI) {

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