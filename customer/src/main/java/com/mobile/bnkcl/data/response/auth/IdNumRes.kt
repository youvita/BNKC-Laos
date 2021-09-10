package com.mobile.bnkcl.data.response.auth

import com.google.gson.annotations.SerializedName

class IdNumRes(
    @SerializedName("verified")
    var verified : Boolean? = false
)