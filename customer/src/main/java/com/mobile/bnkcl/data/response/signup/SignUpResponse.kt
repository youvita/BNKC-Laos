package com.mobile.bnkcl.data.response.signup

import com.google.gson.annotations.SerializedName

class SignUpResponse(
    @SerializedName ("username")
    var username : String? = null
)