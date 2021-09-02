package com.mobile.bnkcl.data.response.code

import com.google.gson.annotations.SerializedName

data class CodesData(

    @SerializedName("code")
    val code: String? = null,

    @SerializedName("title")
    val title: String? = null

)