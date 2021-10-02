package com.mobile.bnkcl.data.repository.auth

import com.bnkc.library.data.network.RemoteDataSource
import com.bnkc.library.data.type.Resource
import com.bnkc.sourcemodule.app.RetrofitBuilder
import com.mobile.bnkcl.data.api.auth.AuthAPI
import com.mobile.bnkcl.data.request.auth.*
import com.mobile.bnkcl.data.request.signup.PreSignUpRequest
import com.mobile.bnkcl.data.response.auth.IdNumRes
import com.mobile.bnkcl.data.response.auth.LoginResponse
import com.mobile.bnkcl.data.response.auth.PreLoginResponse
import com.mobile.bnkcl.data.response.signup.PreSignUpResponse
import com.mobile.bnkcl.data.response.signup.SignUpResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import retrofit2.Response

class AuthRepo(okHttpClient: OkHttpClient) {

    private val authAPI: AuthAPI by lazy { RetrofitBuilder(okHttpClient).getRetrofit().create(AuthAPI::class.java) }

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

    fun preSignUp(preSignUpRequest: PreSignUpRequest): Flow<Resource<PreSignUpResponse>> = flow {
        try {
            delay(1000)
            val request = object : RemoteDataSource<PreSignUpResponse>(){
                override suspend fun createCall(): Response<PreSignUpResponse> {
                    return authAPI.preSignUp(preSignUpRequest)
                }
            }
            request.networkRequest()
            emit(request.asLiveData().value!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun signUpUser(signUpRequest: SignUpRequest): Flow<Resource<SignUpResponse>> = flow {
        try {
            delay(1000)
            val request = object : RemoteDataSource<SignUpResponse>() {
                override suspend fun createCall(): Response<SignUpResponse> {
                    return authAPI.signUpUser(signUpRequest)
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

    fun verifyIdentification(idNumReq: IdNumReq) : Flow<Resource<IdNumRes>> = flow {
        try {
            val req = object  : RemoteDataSource<IdNumRes>(){
                override suspend fun createCall(): Response<IdNumRes> {
                    return authAPI.verifyIdentification(
                        idNumReq.username!!,
                        idNumReq.identification_number!!
                    )
                }
            }
            req.networkRequest()
            emit(req.asLiveData().value!!)
        }catch (e:java.lang.Exception){
            e.printStackTrace()
        }
    }
    fun forgetPin(forgetPinRequest: ForgetPinRequest): Flow<Resource<Unit>> = flow {
        try {
            delay(1000)
            val request = object : RemoteDataSource<Unit>() {
                override suspend fun createCall(): Response<Unit> {
                    return authAPI.forgetPin(forgetPinRequest)
                }
            }
            request.networkRequest()
            emit(request.asLiveData().value!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}