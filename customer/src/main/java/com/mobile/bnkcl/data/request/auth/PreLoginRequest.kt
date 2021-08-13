package com.mobile.bnkcl.data.request.auth

import com.google.gson.annotations.SerializedName

data class PreLoginRequest(

    @SerializedName("username")
    var username: String? = null,

    @SerializedName("pin_id")
    var pin_id: String? = null

)