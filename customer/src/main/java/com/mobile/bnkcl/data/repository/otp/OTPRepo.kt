package com.mobile.bnkcl.data.repository.otp

import com.bnkc.library.data.network.RemoteDataSource
import com.bnkc.library.data.type.Resource
import com.bnkc.sourcemodule.app.RetrofitBuilder
import com.mobile.bnkcl.data.api.otp.OTPApi
import com.mobile.bnkcl.data.request.otp.OTPVerifyRequest
import com.mobile.bnkcl.data.request.otp.SendOTPRequest
import com.mobile.bnkcl.data.response.otp.OTPVerifyResponse
import com.mobile.bnkcl.data.response.otp.SendOTPResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import retrofit2.Response


class OTPRepo(okHttpClient: OkHttpClient) {

    private val otpApi: OTPApi by lazy { RetrofitBuilder(okHttpClient).getRetrofit().create(OTPApi::class.java) }

    fun sendOTP(sendOTPRequest: SendOTPRequest): Flow<Resource<SendOTPResponse>> = flow {
        try {
            delay(1000)
            val request = object : RemoteDataSource<SendOTPResponse>(){
                override suspend fun createCall(): Response<SendOTPResponse> {
                    return otpApi.sendOTP(sendOTPRequest)
                }
            }
            request.networkRequest()
            emit(request.asLiveData().value!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun OTPVerify(otpVerifyRequest: OTPVerifyRequest): Flow<Resource<OTPVerifyResponse>> = flow {
        try {
            delay(1000)
            val request = object : RemoteDataSource<OTPVerifyResponse>() {
                override suspend fun createCall(): Response<OTPVerifyResponse> {
                    return otpApi.verifyOTP(otpVerifyRequest)
                }
            }
            request.networkRequest()
            emit(request.asLiveData().value!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}