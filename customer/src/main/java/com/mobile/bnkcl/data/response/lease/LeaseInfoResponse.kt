package com.mobile.bnkcl.data.response.lease

import com.google.gson.annotations.SerializedName

data class LeaseInfoResponse (

    @SerializedName("coming_repayment_amount")
    val comingRepaymentAmount: String? = null,

    @SerializedName("lease_info")
    val leaseInfoData: LeaseInfoData? = null,

    @SerializedName("coming_repayment_info")
    val comingRepaymentInfoData: ComingRepaymentInfoData? = null

)