package com.mobile.bnkcl.data.request.lease.calcculate

import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.google.gson.annotations.SerializedName

data class LeaseCalculateReq (

    @SerializedName("lease_amount")
    var lease_amount: ObservableField<String>? = null,

    @SerializedName("interest_rate")
    var interest_rate: ObservableInt ? = null,

    @SerializedName("repayment_term")
    var repayment_term: ObservableInt? = null

)