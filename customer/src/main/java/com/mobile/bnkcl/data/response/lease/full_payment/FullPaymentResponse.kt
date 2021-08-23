package com.mobile.bnkcl.data.response.lease.full_payment

import com.google.gson.annotations.SerializedName

data class FullPaymentResponse (

    @SerializedName("full_repayment")
    val fullRepayment: String? = null,

    @SerializedName("principal")
    val principal: String? = null,

    @SerializedName("interest")
    val interest: String? = null,

    @SerializedName("early_repayment_fee")
    val earlyRepaymentFee: String? = null,

    @SerializedName("other_amount")
    val otherAmount: String? = null,

    @SerializedName("others")
    val othersData: List<OthersData>? = null

)