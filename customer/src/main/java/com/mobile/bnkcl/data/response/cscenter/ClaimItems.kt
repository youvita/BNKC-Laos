package com.mobile.bnkcl.data.response.cscenter

import com.google.gson.annotations.SerializedName

data class ClaimItems (
    @SerializedName("id")
    var id: Int? = null,

    @SerializedName("category")
    var category: String? = null,

    @SerializedName("category_name")
    var category_name: String? = null,

    @SerializedName("title")
    var title: String? = null,

    @SerializedName("created_on")
    var created_on: String? = null,

    @SerializedName("replied_on")
    var replied_on: String? = null

)
