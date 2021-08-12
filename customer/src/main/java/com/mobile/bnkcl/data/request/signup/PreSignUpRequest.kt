package com.mobile.bnkcl.data.request.signup

import com.google.gson.annotations.SerializedName

class PreSignUpRequest (
    @SerializedName("username")
    var username : String? = null,

    @SerializedName("pin_id")
    var pin_id : String?= null
        )