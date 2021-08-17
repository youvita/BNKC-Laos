package com.mobile.bnkcl.data.response.dashboard

import com.google.gson.annotations.SerializedName

data class DashboardResponse (

    @SerializedName("summary")
    val summary: SummaryData? = null,

    @SerializedName("my_leases")
    val my_leases: ArrayList<MyLeasesData>? = null

)