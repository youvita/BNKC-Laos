package com.mobile.bnkcl.data.api.otp

import com.mobile.bnkcl.data.request.otp.OTPVerifyRequest
import com.mobile.bnkcl.data.request.otp.SendOTPRequest
import com.mobile.bnkcl.data.response.otp.OTPVerifyResponse
import com.mobile.bnkcl.data.response.otp.SendOTPResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface OTPApi {

    @POST("api/ca/otp/send")
    suspend fun sendOTP(@Body sendOTPRequest: SendOTPRequest): Response<SendOTPResponse>

    @POST("api/ca/otp/verify")
    suspend fun verifyOTP(@Body otpVerifyRequest: OTPVerifyRequest): Response<OTPVerifyResponse>

}