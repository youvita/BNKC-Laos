package com.mobile.bnkcl.data.request.cscenter

import com.google.gson.annotations.SerializedName

data class ClaimReq(

        @SerializedName("category")
        var category: String? = null,

        @SerializedName("title")
        var title: String? = null,

        @SerializedName("content")
        var content: String? = null,
)