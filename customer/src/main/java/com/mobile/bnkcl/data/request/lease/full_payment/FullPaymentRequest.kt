package com.mobile.bnkcl.data.request.lease.full_payment

import com.google.gson.annotations.SerializedName

data class FullPaymentRequest(

    @SerializedName("contract_no")
    var contract_no: String? = null,

    @SerializedName("repayment_date")
    var repayment_date: String? = null,

    @SerializedName("payment_date.dir")
    var payment_date_dir: String? = null

)