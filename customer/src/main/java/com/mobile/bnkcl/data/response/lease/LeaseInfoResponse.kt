package com.mobile.bnkcl.data.response.lease

import com.google.gson.annotations.SerializedName

data class LeaseInfoResponse (

    @SerializedName("coming_repayment_amount")
    val comingRepaymentAmount: String? = null,

    @SerializedName("summary")
    val leaseInfoData: LeaseInfoData? = null,

    @SerializedName("my_leases")
    val comingRepaymentInfoData: ComingRepaymentInfoData? = null

)