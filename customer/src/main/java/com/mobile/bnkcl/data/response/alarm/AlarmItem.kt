package com.mobile.bnkcl.data.response.alarm

import com.google.gson.annotations.SerializedName

data class AlarmItem(

    @SerializedName("id")
    var id: Int,

    @SerializedName("title")
    var title: String,

    @SerializedName("content")
    var content: String,

    @SerializedName("created_on")
    var created_on: String,

    @SerializedName("status")
    var status: Char,

    @SerializedName("push_status")
    var push_status: Char

)