package com.mobile.bnkcl.data.response.notice

import com.google.gson.annotations.SerializedName

class NoticeDataResponse {
    @SerializedName("notices")
    var notices : List<NoticeItems>? = null
}