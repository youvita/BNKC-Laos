package com.mobile.bnkcl.data.request.auth

import com.google.gson.annotations.SerializedName

data class LoginRequestNoAuth(

    @SerializedName("username")
    var username: String? = null,

    @SerializedName("password")
    var password: String? = null,

    @SerializedName("device_info")
    var device_info: DeviceInfo? = null

)