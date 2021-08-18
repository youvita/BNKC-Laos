package com.mobile.bnkcl.data.request.signup

import com.google.gson.annotations.SerializedName

class IdentificationNumRequest (
        @SerializedName("username")
        var username : String? = null,

        @SerializedName("identification_number")
        var identification_number : Number?= null
)