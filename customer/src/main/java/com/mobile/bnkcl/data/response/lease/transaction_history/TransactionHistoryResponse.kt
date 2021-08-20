package com.mobile.bnkcl.data.response.lease.transaction_history

import com.google.gson.annotations.SerializedName

data class TransactionHistoryResponse (

    @SerializedName("transaction_history")
    val transactionHistory: List<TransactionHistoryData>? = null

)