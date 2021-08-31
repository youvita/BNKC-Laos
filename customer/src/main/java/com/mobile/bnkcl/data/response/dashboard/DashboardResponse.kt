package com.mobile.bnkcl.data.response.dashboard

import com.google.gson.annotations.SerializedName

data class DashboardResponse (


    @SerializedName("count_application")
    val countApplication: Int? = null,

    @SerializedName("count_screening")
    val countScreening: Int? = null,

    @SerializedName("count_result")
    val countResult: Int? = null,

    @SerializedName("lease_in_use")
    val myLeases: ArrayList<MyLeasesData>? = null

)