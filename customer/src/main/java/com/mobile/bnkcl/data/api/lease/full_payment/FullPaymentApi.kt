package com.mobile.bnkcl.data.api.lease.full_payment

import com.mobile.bnkcl.data.response.lease.full_payment.FullPaymentResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FullPaymentApi {

    @GET("api/ca/my-leases/{contract_no}/repayment")
    suspend fun getTotalLeaseSchedule(@Path("contract_no") contract_no: String,
                                      @Query("repayment_date") repayment_date: String?,
                                      @Query("payment_date.dir") sort_by: String? ): Response<FullPaymentResponse>

}