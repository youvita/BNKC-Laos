package com.mobile.bnkcl.data.response.area

import com.google.gson.annotations.SerializedName

class AreaRes {
    @SerializedName("areas")
    val areas: List<AreaItems>? = null
    @SerializedName("last")
    val last: Boolean? = null
    @SerializedName("first")
    val first: Boolean? = null
    @SerializedName("size")
    val size: Int? = null
    @SerializedName("number")
    val number: Int? = null
    @SerializedName("total_pages")
    val total_pages: Int? = null
    @SerializedName("total_elements")
    val total_elements: Int? = null
}