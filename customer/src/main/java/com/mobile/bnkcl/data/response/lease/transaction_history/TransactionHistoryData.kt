package com.mobile.bnkcl.data.response.lease.transaction_history

import com.google.gson.annotations.SerializedName

data class TransactionHistoryData (

    @SerializedName("payment_date")
    val paymentDate: String? = null,

    @SerializedName("paid_amount")
    val paidAmount: String? = null,

    @SerializedName("penalty")
    val penalty: String? = null,

    @SerializedName("interest")
    val interest: String? = null,

    @SerializedName("principal")
    val principal: String? = null

)