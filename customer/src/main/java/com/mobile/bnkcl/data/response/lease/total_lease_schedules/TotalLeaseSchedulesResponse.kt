package com.mobile.bnkcl.data.response.lease.total_lease_schedules

import com.google.gson.annotations.SerializedName

data class TotalLeaseSchedulesResponse (

    @SerializedName("total_principal")
    val totalPrincipal: String? = null,

    @SerializedName("total_interest")
    val totalInterest: String? = null,

    @SerializedName("total_lease_schedules")
    val totalLeaseSchedulesData: TotalLeaseSchedulesData? = null

)