package com.mobile.bnkcl.data.request.findoffice

import com.google.gson.annotations.SerializedName

data class BranchRequest (

    @SerializedName("area_id")
    var area_id: String? = null,

    @SerializedName("page_number")
    var page_number: Int? = null,

    @SerializedName("page_size")
    var page_size: Int? = null,

    @SerializedName("sort_by")
    var sort_by: String? = null,

)