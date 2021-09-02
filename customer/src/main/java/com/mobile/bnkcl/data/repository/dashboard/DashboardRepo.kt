package com.mobile.bnkcl.data.repository.dashboard

import android.content.Context
import com.bnkc.library.data.network.RemoteDataSource
import com.bnkc.library.data.type.Resource
import com.bnkc.sourcemodule.app.RetrofitBuilder
import com.mobile.bnkcl.data.api.code.CodesApi
import com.mobile.bnkcl.data.api.dashboard.DashboardApi
import com.mobile.bnkcl.data.response.code.CodesResponse
import com.mobile.bnkcl.data.response.dashboard.DashboardResponse
import com.mobile.bnkcl.data.response.dashboard.LeaseApplicationResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import retrofit2.Response
import javax.inject.Inject

class DashboardRepo @Inject constructor(context: Context, okHttpClient: OkHttpClient) {

    private val dashboardApi: DashboardApi by lazy {
        RetrofitBuilder(context, okHttpClient).getRetrofit().create(DashboardApi::class.java)
    }

    private val codesApi: CodesApi by lazy {
        RetrofitBuilder(context, okHttpClient).getRetrofit().create(CodesApi::class.java)
    }

    fun getDashboard(): Flow<Resource<DashboardResponse>> = flow {
        delay(1000)
        try {
            val request = object: RemoteDataSource<DashboardResponse>(){
                override suspend fun createCall(): Response<DashboardResponse> {
                    return dashboardApi.getDashboard()
                }
            }
            request.networkRequest()
            emit(request.asLiveData().value!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getLeaseApplication(lease_request_status : String): Flow<Resource<LeaseApplicationResponse>> = flow {
        delay(1000)
        try {
            val request = object: RemoteDataSource<LeaseApplicationResponse>(){
                override suspend fun createCall(): Response<LeaseApplicationResponse> {
                    return dashboardApi.getLeaseApplication(lease_request_status)
                }
            }
            request.networkRequest()
            emit(request.asLiveData().value!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getCodes(group_id : String): Flow<Resource<CodesResponse>> = flow {
        delay(1000)
        try {
            val request = object: RemoteDataSource<CodesResponse>(){
                override suspend fun createCall(): Response<CodesResponse> {
                    return codesApi.getCodes(group_id)
                }
            }
            request.networkRequest()
            emit(request.asLiveData().value!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}