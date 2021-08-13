package com.mobile.bnkcl.data.response.auth

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @SerializedName("token")
    var token: String? = null,

    @SerializedName("cust_no")
    var cust_no: String? = null

)