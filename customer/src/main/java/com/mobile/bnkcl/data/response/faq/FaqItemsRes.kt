package com.mobile.bnkcl.data.response.faq

import com.google.gson.annotations.SerializedName

class FaqItemsRes (
        @SerializedName("id")
        var id: Int? = null,

        @SerializedName("content")
        var content: String? = null,

        @SerializedName("title")
        var title: String? = null
)