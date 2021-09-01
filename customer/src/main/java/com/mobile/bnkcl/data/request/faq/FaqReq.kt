package com.mobile.bnkcl.data.request.faq

import com.google.gson.annotations.SerializedName

class FaqReq (
        @SerializedName("page_number")
        var page_number: Int? = null,

        @SerializedName("page_size")
        var page_size: Int? = null,

        @SerializedName("sort_by")
        var sort_by: String? = null
)