package com.mobile.bnkcl.data.request.otp

import com.google.gson.annotations.SerializedName

data class OTPVerifyRequest(

    @SerializedName("pin")
    var pin: String? = null,

    @SerializedName("pin_id")
    var pin_id: String? = null

)