package com.mobile.bnkcl.data.response.dashboard

import com.google.gson.annotations.SerializedName

data class DashboardResponse (

    @SerializedName("my_lease_request")
    val summary: SummaryData? = null,

    @SerializedName("lease_in_use")
    val myLeases: ArrayList<MyLeasesData>? = null

)