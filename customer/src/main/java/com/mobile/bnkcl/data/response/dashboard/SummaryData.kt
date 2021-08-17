package com.mobile.bnkcl.data.response.dashboard

import com.google.gson.annotations.SerializedName

data class SummaryData(

    @SerializedName("count_pending")
    val count_pending: Int? = null,

    @SerializedName("count_in_progress")
    val count_in_progress: Int? = null,

    @SerializedName("count_done")
    val count_done: Int? = null,

    @SerializedName("count_activated")
    val count_activated: Int? = null,

    )