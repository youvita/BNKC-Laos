package com.mobile.bnkcl.data.api.lease.transaction

import com.mobile.bnkcl.data.response.lease.transaction_history.TransactionHistoryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TransactionHistoryApi {

    @GET("api/ca/my-leases/{contract_no}/transactions")
    suspend fun getTransactionHistory(@Path("contract_no") contract_no: String,
                                      @Query("payment_date.dir") sort_by: String? ): Response<TransactionHistoryResponse>

}