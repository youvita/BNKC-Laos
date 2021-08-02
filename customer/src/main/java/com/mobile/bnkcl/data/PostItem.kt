package com.mobile.bnkcl.data


import com.google.gson.annotations.SerializedName

data class PostItem(

    @SerializedName("body")
    val body: String? = null,

    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("title")
    val title: String? = null,

    @SerializedName("userId")
    val userId: Int? = null
)