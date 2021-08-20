package com.mobile.bnkcl.data.request.lease.transaction

import com.google.gson.annotations.SerializedName

data class TransactionHistoryRequest(

    @SerializedName("contract_no")
    var contract_no: String? = null,

    @SerializedName("payment_date.dir")
    var payment_date_dir: String? = null

)