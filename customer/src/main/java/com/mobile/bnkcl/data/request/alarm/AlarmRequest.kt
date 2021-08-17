package com.mobile.bnkcl.data.request.alarm

import com.google.gson.annotations.SerializedName

data class AlarmRequest(

    @SerializedName("page_number")
    var page_number: Int,

    @SerializedName("page_size")
    var page_size: Int,

    @SerializedName("sort_by")
    var sort_by: String? = null

)