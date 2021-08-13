package com.mobile.bnkcl.data.response.otp

import com.google.gson.annotations.SerializedName

data class SendOTPResponse(

    @SerializedName("pin")
    var pin: String? = null,

    @SerializedName("pin_id")
    var pin_id: String? = null,

    @SerializedName("lifetime")
    var lifetime: Int? = 0
)