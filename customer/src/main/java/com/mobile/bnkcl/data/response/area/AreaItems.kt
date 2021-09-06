package com.mobile.bnkcl.data.response.area

import com.google.gson.annotations.SerializedName

data class AreaItems (
    @SerializedName("id")
     var id: Int = 0,

    @SerializedName("name")
     var name: String? = null,

    @SerializedName ("erp_code")
     var erp_code : String? = null,

    @SerializedName("iso_code")
     var iso_code: String? = null,

    @SerializedName("category")
    var category: String? = null,

    @SerializedName("alias1")
    var alias1: String? = null,

    @SerializedName("branch_count")
     var branch_count: String? = null
)