package com.mobile.bnkcl.data.repository.lease.total_schedule

import android.content.Context
import com.bnkc.library.data.network.RemoteDataSource
import com.bnkc.library.data.type.Resource
import com.bnkc.sourcemodule.app.RetrofitBuilder
import com.mobile.bnkcl.data.api.lease.total_schedule.TotalLeaseScheduleApi
import com.mobile.bnkcl.data.request.lease.total_schedule.TotalLeaseScheduleRequest
import com.mobile.bnkcl.data.response.lease.total_lease_schedules.TotalLeaseScheduleResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import retrofit2.Response
import javax.inject.Inject

class TotalLeaseScheduleRepo @Inject constructor(context: Context, okHttpClient: OkHttpClient) {

    private val transactionApi: TotalLeaseScheduleApi by lazy {
        RetrofitBuilder(context, okHttpClient).getRetrofit().create(TotalLeaseScheduleApi::class.java)
    }

    fun getTotalLeaseSchedule(transactionHistoryRequest: TotalLeaseScheduleRequest): Flow<Resource<TotalLeaseScheduleResponse>> = flow {
        delay(1000)
        try {
            val request = object: RemoteDataSource<TotalLeaseScheduleResponse>(){
                override suspend fun createCall(): Response<TotalLeaseScheduleResponse> {
                    return transactionApi.getTotalLeaseSchedule(transactionHistoryRequest.contract_no!!, transactionHistoryRequest.payment_date_dir!!)
                }
            }
            request.networkRequest()
            emit(request.asLiveData().value!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}