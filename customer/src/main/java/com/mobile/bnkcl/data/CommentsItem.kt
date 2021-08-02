package com.mobile.bnkcl.data

import com.google.gson.annotations.SerializedName

data class CommentsItem(

    @SerializedName("postId")
    var postId: Int? = null,

    @SerializedName("id")
    var id: Int? = null,

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("email")
    var email: String? = null,

    @SerializedName("body")
    var body: String? = null
)
