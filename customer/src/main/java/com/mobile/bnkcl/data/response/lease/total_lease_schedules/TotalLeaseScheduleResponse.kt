package com.mobile.bnkcl.data.response.lease.total_lease_schedules

import com.google.gson.annotations.SerializedName

data class TotalLeaseScheduleResponse (

    @SerializedName("total_principal")
    val totalPrincipal: String? = null,

    @SerializedName("total_interest")
    val totalInterest: String? = null,

    @SerializedName("total_lease_schedules")
    val totalLeaseScheduleData: TotalLeaseScheduleData? = null

)