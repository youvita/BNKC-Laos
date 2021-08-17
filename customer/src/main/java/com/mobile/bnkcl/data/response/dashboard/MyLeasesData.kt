package com.mobile.bnkcl.data.response.dashboard

import com.google.gson.annotations.SerializedName

data class MyLeasesData(

    @SerializedName("contract_no")
    val contract_no: String? = null,

    @SerializedName("lease_amount_type")
    val lease_amount_type: String? = null,

    @SerializedName("lease_balance")
    val lease_balance: String? = null,

    @SerializedName("repayment_date")
    val repayment_date: String? = null,

    @SerializedName("repayment_amount")
    val repayment_amount: String? = null,

    )