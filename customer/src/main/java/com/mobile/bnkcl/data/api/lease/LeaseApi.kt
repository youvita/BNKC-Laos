package com.mobile.bnkcl.data.api.lease

import com.mobile.bnkcl.data.request.auth.LoginRequestNoAuth
import com.mobile.bnkcl.data.request.lease.apply.ApplyLeaseRequest
import com.mobile.bnkcl.data.request.lease.calcculate.LeaseCalculateReq
import com.mobile.bnkcl.data.response.lease.ItemResponse
import com.mobile.bnkcl.data.response.lease.LeaseInfoResponse
import com.mobile.bnkcl.data.response.lease.apply.ApplyLeaseResponse
import com.mobile.bnkcl.data.response.lease.calculate.LeaseCalResponse
import com.mobile.bnkcl.data.response.lease.full_payment.FullPaymentResponse
import com.mobile.bnkcl.data.response.lease.total_lease_schedules.TotalLeaseScheduleResponse
import com.mobile.bnkcl.data.response.lease.transaction_history.TransactionHistoryResponse
import retrofit2.Response
import retrofit2.http.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LeaseApi {

    @GET("api/ca/my-leases/{contract_no}")
    suspend fun getLeaseInfo(
        @Path("contract_no") contract_no: String
    ): Response<LeaseInfoResponse>

    @GET("api/ca/my-leases/{contract_no}/repayment")
    suspend fun getFullPayment(
        @Path("contract_no") contract_no: String,
        @Query("repayment_date") repayment_date: String?,
        @Query("payment_date.dir") sort_by: String?
    ): Response<FullPaymentResponse>

    @GET("api/ca/my-leases/{contract_no}/schedules")
    suspend fun getTotalLeaseSchedule(
        @Path("contract_no") contract_no: String,
        @Query("payment_date.dir") sort_by: String?
    ): Response<TotalLeaseScheduleResponse>

    @GET("api/ca/my-leases/{contract_no}/transactions")
    suspend fun getTransactionHistory(
        @Path("contract_no") contract_no: String,
        @Query("payment_date.dir") sort_by: String?
    ): Response<TransactionHistoryResponse>

    @POST("/api/ca/leases/calculation")
    suspend fun getLeaseCalculate(
        @Body leaseCalculateReq: LeaseCalculateReq
    ): Response<LeaseCalResponse>

    @POST("api/ca/leases/applications")
    suspend fun applyLease(
        @Body applyLeaseRequest: ApplyLeaseRequest
    ): Response<ApplyLeaseResponse>

    @GET("api/ca/common/codes")
    suspend fun getCodeLease(
        @Query("group_id") group_id: String
    ): Response<ItemResponse>

}