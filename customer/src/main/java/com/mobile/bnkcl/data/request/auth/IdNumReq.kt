package com.mobile.bnkcl.data.request.auth

import com.google.gson.annotations.SerializedName

class IdNumReq(
        @SerializedName("username")
        var username: String? = null,

        @SerializedName("identification_number")
        var identification_number: String? = null
)