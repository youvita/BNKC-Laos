package com.mobile.bnkcl.data.request.user

import com.google.gson.annotations.SerializedName

class ResetPasswordRequest {
    @SerializedName("password")
    var password : String? = null

    @SerializedName("session_id")
    var session_id : String? = null
}