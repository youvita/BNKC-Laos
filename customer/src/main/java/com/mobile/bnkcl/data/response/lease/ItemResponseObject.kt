package com.mobile.bnkcl.data.response.lease

import com.google.gson.annotations.SerializedName

data class ItemResponseObject(
    @SerializedName("code")
    val code: String? = null,

    @SerializedName("title")
    val title: String? = null
)
