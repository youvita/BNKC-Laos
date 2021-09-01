package com.mobile.bnkcl.data.response.faq

import com.google.gson.annotations.SerializedName

class FaqRes(
        @SerializedName("last")
        var last: Boolean? = null,

        @SerializedName("first")
        var first: Boolean? = null,

        @SerializedName("size")
        var size: Int? = null,

        @SerializedName("number")
        var number: Int? = null,

        @SerializedName("empty")
        var empty: Boolean? = null,

        @SerializedName("total_pages")
        var total_pages: Int? = null,

        @SerializedName("total_elements")
        var total_elements: Int? = null,

        @SerializedName("number_elements")
        var number_elements: Int? = null,

        @SerializedName("faqs")
        var faqs: ArrayList<FaqItemsRes>? = null,
)