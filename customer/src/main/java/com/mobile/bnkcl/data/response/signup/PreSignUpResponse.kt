package com.mobile.bnkcl.data.response.signup

import com.google.gson.annotations.SerializedName

class PreSignUpResponse (
    @SerializedName("session_id")
    var session_id : String? = null
        )