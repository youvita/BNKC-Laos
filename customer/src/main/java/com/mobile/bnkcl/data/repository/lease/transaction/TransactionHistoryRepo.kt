package com.mobile.bnkcl.data.repository.lease.transaction

import android.content.Context
import com.bnkc.library.data.network.RemoteDataSource
import com.bnkc.library.data.type.Resource
import com.bnkc.sourcemodule.app.RetrofitBuilder
import com.mobile.bnkcl.data.api.lease.transaction.TransactionHistoryApi
import com.mobile.bnkcl.data.request.lease.transaction.TransactionHistoryRequest
import com.mobile.bnkcl.data.response.lease.transaction_history.TransactionHistoryResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import retrofit2.Response
import javax.inject.Inject

class TransactionHistoryRepo @Inject constructor(context: Context, okHttpClient: OkHttpClient) {

    private val transactionApi: TransactionHistoryApi by lazy {
        RetrofitBuilder(context, okHttpClient).getRetrofit()
            .create(TransactionHistoryApi::class.java)
    }

    fun getTransactionHistory(transactionHistoryRequest: TransactionHistoryRequest): Flow<Resource<TransactionHistoryResponse>> =
        flow {
            delay(1000)
            try {
                val request = object : RemoteDataSource<TransactionHistoryResponse>() {
                    override suspend fun createCall(): Response<TransactionHistoryResponse> {
                        return transactionApi.getTransactionHistory(
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