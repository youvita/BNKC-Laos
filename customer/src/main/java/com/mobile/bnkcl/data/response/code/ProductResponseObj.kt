package com.mobile.bnkcl.data.response.code

import com.google.gson.annotations.SerializedName

class ProductResponseObj (

    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("erpCode")
    val erpCode: String? = null,

    @SerializedName("category")
    val category: String? = null,

        )