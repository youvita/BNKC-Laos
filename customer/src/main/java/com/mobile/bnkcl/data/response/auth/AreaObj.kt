package com.mobile.bnkcl.data.response.auth

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class AreaObj : Serializable {
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("name")
    var name: String? = null

    @SerializedName ("erp_code")
    var erp_code : String? = null

    @SerializedName("iso_code")
    var iso_code: String? = null

    @SerializedName("alias2")
    var alias2: String? = null

    @SerializedName("alias1")
    var alias1: String? = null
}