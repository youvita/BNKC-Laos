package com.mobile.bnkcl.data.repository.dashboard

import android.content.Context
import com.bnkc.library.data.network.RemoteDataSource
import com.bnkc.library.data.type.Resource
import com.bnkc.sourcemodule.app.RetrofitBuilder
import com.mobile.bnkcl.data.api.dashboard.DashboardApi
import com.mobile.bnkcl.data.response.dashboard.DashboardResponse
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

}