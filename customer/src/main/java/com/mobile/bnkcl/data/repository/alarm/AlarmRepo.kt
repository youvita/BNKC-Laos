package com.mobile.bnkcl.data.repository.alarm

import android.content.Context
import com.bnkc.library.data.network.RemoteDataSource
import com.bnkc.library.data.type.Resource
import com.bnkc.sourcemodule.app.RetrofitBuilder
import com.mobile.bnkcl.data.api.alarm.AlarmAPI
import com.mobile.bnkcl.data.request.alarm.AlarmRequest
import com.mobile.bnkcl.data.response.alarm.AlarmResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import retrofit2.Response

class AlarmRepo(context: Context, okHttpClient: OkHttpClient) {

    private val alarmApi: AlarmAPI by lazy { RetrofitBuilder(context, okHttpClient).getRetrofit().create(AlarmAPI::class.java) }

    fun getAlarmList(alarmRequest: AlarmRequest): Flow<Resource<AlarmResponse>> = flow {
        try {
            delay(1000)
            val request = object : RemoteDataSource<AlarmResponse>(){
                override suspend fun createCall(): Response<AlarmResponse> {
                    return alarmApi.getAlarmList(alarmRequest.page_number, alarmRequest.page_size, alarmRequest.sort_by)
                }
            }
            request.networkRequest()
            emit(request.asLiveData().value!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}