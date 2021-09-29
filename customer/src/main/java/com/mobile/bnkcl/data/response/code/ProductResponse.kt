package com.mobile.bnkcl.data.response.code

import com.google.gson.annotations.SerializedName

data class ProductResponse (

    @SerializedName("products")
    val products: List<ProductResponseObj>? = null

)