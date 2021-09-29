package com.mobile.bnkcl.data.request.lease.calcculate

import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.google.gson.annotations.SerializedName

data class LeaseCalculateReq (

    @SerializedName("lease_amount")
    var lease_amount: String? = null,

    @SerializedName("interest_rate")
    var interest_rate: Float ? = null,

    @SerializedName("repayment_term")
    var repayment_term: Int? = null

)