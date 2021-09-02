package com.mobile.bnkcl.data.response.lease.calculate

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LeaseCalResponse(

    @SerializedName("monthly_repayment")
    var monthly_repayment: String? = null,

    @SerializedName("total_principle_paid")
    var total_principle_paid: String? = null,

    @SerializedName("total_interest_paid")
    var total_interest_paid: String? = null,

    @SerializedName("installations")
    var installations: List<LeaseCalResObj>? = null

) : Serializable
