package com.mobile.bnkcl.data.request.otp

import com.google.gson.annotations.SerializedName

data class SendOTPRequest(

    @SerializedName("to")
    var to: String? = null

)