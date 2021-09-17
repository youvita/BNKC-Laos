package com.mobile.bnkcl.data.request.auth

import com.google.gson.annotations.SerializedName

class ForgetPinRequest {

    @SerializedName("session_id")
    var session_id: String? = null

    @SerializedName("username")
    var username: String? = null

    @SerializedName("password")
    var password: String? = null

}