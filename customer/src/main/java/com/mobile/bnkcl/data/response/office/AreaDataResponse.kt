package com.mobile.bnkcl.data.response.office

import com.google.gson.annotations.SerializedName

data class AreaDataResponse(
    @SerializedName("id")
     var id: Int? = null,

    @SerializedName("name")
     var name: String? = null,

    @SerializedName ("erp_code")
     var erp_code : String? = null,

    @SerializedName("iso_code")
     var iso_code: String? = null,

//    @SerializedName("alias1")
//    var alias1: String? = null,

    @SerializedName("branch_count")
     var branch_count: Int? = null,

//    @SerializedName("alias2")
//    var alias2: String? = null

    @SerializedName("parentId")
    var parentId: Int? = null,

    @SerializedName("category")
    var category: String? = null
)