package com.mobile.bnkcl.data.repository.lease

import android.content.Context
import com.bnkc.library.data.network.RemoteDataSource
import com.bnkc.library.data.type.Resource
import com.bnkc.sourcemodule.app.RetrofitBuilder
import com.mobile.bnkcl.data.api.lease.LeaseApi
import com.mobile.bnkcl.data.response.lease.LeaseInfoResponse
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
            val request = object: RemoteDataSource<LeaseInfoResponse>(){
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

}