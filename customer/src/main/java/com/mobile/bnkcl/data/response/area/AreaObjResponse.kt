package com.mobile.bnkcl.data.response.area

import com.google.gson.annotations.SerializedName

class AreaObjResponse {
    @SerializedName("id")
     var id: Long = 0

    @SerializedName("name")
     var name: String? = null

    @SerializedName ("erp_code")
     var erp_code : String? = null

    @SerializedName("iso_code")
     var iso_code: String? = null

    @SerializedName("alias1")
    var alias1: String? = null

    @SerializedName("branch_count")
     var branch_count: String? = null

    @SerializedName("alias2")
    var alias2: String? = null
}