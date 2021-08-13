package com.mobile.bnkcl.data.response.auth

import com.google.gson.annotations.SerializedName

data class PreLoginResponse(

    @SerializedName("session_id")
    var session_id: String? = null

)