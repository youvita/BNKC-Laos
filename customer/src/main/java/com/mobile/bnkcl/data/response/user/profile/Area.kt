package com.mobile.bnkcl.data.response.user.profile


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Area(

    @SerializedName("id")
    var id: Any? = null,

    @SerializedName("name")
    var name: Any? = null,

    @SerializedName("iso_code")
    var iso_code: Any? = null,

    @SerializedName("erp_code")
    var erp_code: Any? = null,

    @SerializedName("alias1")
    var alias1: Any? = null,

    @SerializedName("category")
    var category: Any? = null,

    @SerializedName("branches_count")
    var branched_count: Any? = null
): Serializable