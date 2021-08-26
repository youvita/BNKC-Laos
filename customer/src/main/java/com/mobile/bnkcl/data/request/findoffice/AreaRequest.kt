package com.mobile.bnkcl.data.request.findoffice

import com.google.gson.annotations.SerializedName
import com.mobile.bnkcl.data.request.auth.DeviceInfo

data class AreaRequest(

    @SerializedName("parent_id")
    var parent_id: String? = null,

    @SerializedName("category")
    var category: String? = null,

    @SerializedName("search_value")
    var search_value: String? = null,

    @SerializedName("search_condition")
    var search_condition: String? = null,

    @SerializedName("page_number")
    var page_number: String? = null,

    @SerializedName("page_size")
    var page_size: String? = null,

    @SerializedName("category")
    var sort_by: String? = null,

)