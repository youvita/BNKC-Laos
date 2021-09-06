package com.mobile.bnkcl.data.repository.lease

import android.content.Context
import com.bnkc.library.data.network.RemoteDataSource
import com.bnkc.library.data.type.Resource
import com.bnkc.sourcemodule.app.RetrofitBuilder
import com.mobile.bnkcl.data.api.lease.LeaseApi
import com.mobile.bnkcl.data.request.lease.full_payment.FullPaymentRequest
import com.mobile.bnkcl.data.request.lease.total_schedule.TotalLeaseScheduleRequest
import com.mobile.bnkcl.data.request.lease.transaction.TransactionHistoryRequest
import com.mobile.bnkcl.data.request.lease.apply.ApplyLeaseRequest
import com.mobile.bnkcl.data.request.lease.calcculate.LeaseCalculateReq
import com.mobile.bnkcl.data.response.lease.LeaseInfoResponse
import com.mobile.bnkcl.data.response.lease.full_payment.FullPaymentResponse
import com.mobile.bnkcl.data.response.lease.total_lease_schedules.TotalLeaseScheduleResponse
import com.mobile.bnkcl.data.response.lease.transaction_history.TransactionHistoryResponse
import com.mobile.bnkcl.data.response.lease.apply.ApplyLeaseResponse
import com.mobile.bnkcl.data.response.lease.calculate.LeaseCalResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import retrofit2.Response
import javax.inject.Inject

class LeaseRepo @Inject constructor(context: Context, okHttpClient: OkHttpClient) {

    private val leaseApi: LeaseApi by lazy {
        RetrofitBuilder(context, okHttpClient).getRetrofit().create(LeaseApi::class.java)
    }

    fun getLeaseInfo(contractNo: String): Flow<Resource<LeaseInfoResponse>> = flow {
        delay(1000)
        try {
            val request = object : RemoteDataSource<LeaseInfoResponse>() {
                override suspend fun createCall(): Response<LeaseInfoResponse> {
                    return leaseApi.getLeaseInfo(contractNo)
                }
            }
            request.networkRequest()
            emit(request.asLiveData().value!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getLeaseCalculate(leaseCalculateReq: LeaseCalculateReq): Flow<Resource<LeaseCalResponse>> = flow {
        delay(1000)
        try {
            val request = object: RemoteDataSource<LeaseCalResponse>(){
                override suspend fun createCall(): Response<LeaseCalResponse> {
                    return leaseApi.getLeaseCalculate(leaseCalculateReq)
                }
            }
            request.networkRequest()
            emit(request.asLiveData().value!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun applyLease(applyLeaseRequest: ApplyLeaseRequest): Flow<Resource<ApplyLeaseResponse>> = flow {
        delay(1000)
        try {
            val request = object: RemoteDataSource<ApplyLeaseResponse>(){
                override suspend fun createCall(): Response<ApplyLeaseResponse> {
                    return leaseApi.applyLease(applyLeaseRequest)
                }
            }
            request.networkRequest()
            emit(request.asLiveData().value!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun getFullPayment(fullPaymentRequest: FullPaymentRequest): Flow<Resource<FullPaymentResponse>> =
        flow {
            delay(1000)
            try {
                val request = object : RemoteDataSource<FullPaymentResponse>() {
                    override suspend fun createCall(): Response<FullPaymentResponse> {
                        return leaseApi.getFullPayment(
                            fullPaymentRequest.contract_no!!,
                            fullPaymentRequest.repayment_date!!,
                            fullPaymentRequest.payment_date_dir!!
                        )
                    }
                }
                request.networkRequest()
                emit(request.asLiveData().value!!)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    fun getTotalLeaseSchedule(totalLeaseScheduleRequest: TotalLeaseScheduleRequest): Flow<Resource<TotalLeaseScheduleResponse>> =
        flow {
            delay(1000)
            try {
                val request = object : RemoteDataSource<TotalLeaseScheduleResponse>() {
                    override suspend fun createCall(): Response<TotalLeaseScheduleResponse> {
                        return leaseApi.getTotalLeaseSchedule(
                            totalLeaseScheduleRequest.contract_no!!,
                            totalLeaseScheduleRequest.payment_date_dir!!
                        )
                    }
                }
                request.networkRequest()
                emit(request.asLiveData().value!!)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    fun getTransactionHistory(transactionHistoryRequest: TransactionHistoryRequest): Flow<Resource<TransactionHistoryResponse>> =
        flow {
            delay(1000)
            try {
                val request = object : RemoteDataSource<TransactionHistoryResponse>() {
                    override suspend fun createCall(): Response<TransactionHistoryResponse> {
                        return leaseApi.getTransactionHistory(
                            transactionHistoryRequest.contract_no!!,
                            transactionHistoryRequest.payment_date_dir!!
                        )
                    }
                }
                request.networkRequest()
                emit(request.asLiveData().value!!)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
}