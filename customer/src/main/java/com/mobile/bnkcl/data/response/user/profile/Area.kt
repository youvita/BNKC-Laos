package com.mobile.bnkcl.data.response.user.profile


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Area(

    @SerializedName("id")
    var id: String? = null,

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("iso_code")
    var iso_code: String? = null,

    @SerializedName("erp_code")
    var erpCode: String? = null,

    @SerializedName("alias1")
    var alias1: String? = null,

    @SerializedName("category")
    var category: String? = null,

    @SerializedName("branches_count")
    var branched_count: String? = null
): Serializable