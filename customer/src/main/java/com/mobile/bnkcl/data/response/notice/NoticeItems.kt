package com.mobile.bnkcl.data.response.notice

import com.google.gson.annotations.SerializedName

data class NoticeItems (
    @SerializedName("title")
    var title : String?= null,

    @SerializedName("content")
    var content : String?= null,

    @SerializedName("category")
    var category : String?= null,

    @SerializedName("notice_id")
    var notice_id : Number?= null,

    @SerializedName("created_on")
    var created_on : String?= null
)