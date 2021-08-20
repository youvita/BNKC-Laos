package com.mobile.bnkcl.data.response.dashboard

import com.google.gson.annotations.SerializedName

data class MyLeasesData(

    @SerializedName("count_use")
    val countUse: String? = null,

    @SerializedName("contract_no")
    val contractNo: String? = null,

    @SerializedName("lease_type")
    val leaseType: String? = null,

    @SerializedName("lease_balance")
    val leaseBalance: String? = null,

    @SerializedName("coming_repayment_date")
    val comingRepaymentDate: String? = null,

    @SerializedName("total_payment")
    val totalPayment: String? = null

    )