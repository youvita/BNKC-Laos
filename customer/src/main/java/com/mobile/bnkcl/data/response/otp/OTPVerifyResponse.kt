package com.mobile.bnkcl.data.response.otp

import com.google.gson.annotations.SerializedName

data class OTPVerifyResponse(

    @SerializedName("verified")
    var verified: Boolean? = false

)