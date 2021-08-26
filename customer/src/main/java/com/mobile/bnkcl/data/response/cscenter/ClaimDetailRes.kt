package com.mobile.bnkcl.data.response.cscenter

import com.google.gson.annotations.SerializedName

class ClaimDetailRes(

        @SerializedName("category")
        var category: String? = null,

        @SerializedName("title")
        var title: String? = null,

        @SerializedName("content")
        var content: String? = null,

        @SerializedName("created_on")
        var created_on: String? = null,

        @SerializedName("reply")
        var reply: String? = null,

        @SerializedName("replied_on")
        var replied_on: String? = null
)