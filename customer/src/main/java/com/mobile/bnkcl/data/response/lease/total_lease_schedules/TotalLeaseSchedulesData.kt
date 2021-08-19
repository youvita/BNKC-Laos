package com.mobile.bnkcl.data.response.lease.total_lease_schedules

import com.google.gson.annotations.SerializedName

data class TotalLeaseSchedulesData (

    @SerializedName("installment")
    val installment: String? = null,

    @SerializedName("payment_date")
    val paymentDate: String? = null,

    @SerializedName("total_amount")
    val totalAmount: String? = null,

    @SerializedName("principal")
    val principal: String? = null,

    @SerializedName("interest")
    val interest: String? = null,

    @SerializedName("balance")
    val balance: String? = null

)