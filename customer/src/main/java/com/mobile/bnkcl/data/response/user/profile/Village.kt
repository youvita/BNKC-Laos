package com.mobile.bnkcl.data.response.user.profile


import com.google.gson.annotations.SerializedName

data class Village(

    @SerializedName("alias1")
    var alias1: Any? = null,

    @SerializedName("alias2")
    var alias2: Any? = null,

    @SerializedName("erp_code")
    var erp_code: String? = null,

    @SerializedName("id")
    var id: Any? = null,

    @SerializedName("iso_code")
    var iso_code: Any? = null,

    @SerializedName("name")
    var name: Any? = null
)