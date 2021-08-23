package com.mobile.bnkcl.data.repository.lease.full_payment

import android.content.Context
import com.bnkc.library.data.network.RemoteDataSource
import com.bnkc.library.data.type.Resource
import com.bnkc.sourcemodule.app.RetrofitBuilder
import com.mobile.bnkcl.data.api.lease.full_payment.FullPaymentApi
import com.mobile.bnkcl.data.request.lease.full_payment.FullPaymentRequest
import com.mobile.bnkcl.data.response.lease.full_payment.FullPaymentResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import retrofit2.Response
import javax.inject.Inject

class FullPaymentRepo @Inject constructor(context: Context, okHttpClient: OkHttpClient) {

    private val transactionApi: FullPaymentApi by lazy {
        RetrofitBuilder(context, okHttpClient).getRetrofit().create(FullPaymentApi::class.java)
    }

    fun getFullPayment(fullPaymentRequest: FullPaymentRequest): Flow<Resource<FullPaymentResponse>> = flow {
        delay(1000)
        try {
            val request = object: RemoteDataSource<FullPaymentResponse>(){
                override suspend fun createCall(): Response<FullPaymentResponse> {
                    return transactionApi.getTotalLeaseSchedule(fullPaymentRequest.contract_no!!, fullPaymentRequest.repayment_date!!, fullPaymentRequest.payment_date_dir!!)
                }
            }
            request.networkRequest()
            emit(request.asLiveData().value!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}