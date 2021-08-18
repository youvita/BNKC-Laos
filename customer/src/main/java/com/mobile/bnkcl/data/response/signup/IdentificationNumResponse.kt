package com.mobile.bnkcl.data.response.signup

import com.google.gson.annotations.SerializedName

class IdentificationNumResponse (
        @SerializedName("verified")
        var verified : Boolean? = null
        )