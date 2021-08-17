package com.mobile.bnkcl.data.response.alarm

import com.google.gson.annotations.SerializedName

data class AlarmResponse(

    @SerializedName("alarms")
    var alarms: List<AlarmItem>,

    @SerializedName("last")
    var last: Boolean,

    @SerializedName("first")
    var first: Boolean,

    @SerializedName("size")
    var size: Int,

    @SerializedName("number")
    var number: Int,

    @SerializedName("total_pages")
    var total_pages: Int,

    @SerializedName("total_elements")
    var total_elements: Int
)