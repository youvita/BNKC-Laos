package com.mobile.bnkcl.data.repository.notice

import com.bnkc.library.data.network.RemoteDataSource
import com.bnkc.library.data.type.Resource
import com.bnkc.sourcemodule.app.RetrofitBuilder
import com.mobile.bnkcl.data.api.notice.NoticeApi
import com.mobile.bnkcl.data.request.notice.NoticeRequest
import com.mobile.bnkcl.data.response.notice.NoticeDataResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import retrofit2.Response

class NoticeRepo(okHttpClient: OkHttpClient) {
    private val noticeApi: NoticeApi by lazy {
        RetrofitBuilder(okHttpClient).getRetrofit().create(NoticeApi::class.java)
    }

    fun getNoticeData(request: NoticeRequest): Flow<Resource<NoticeDataResponse>> = flow {
        delay(1000)
        try {
            val request = object: RemoteDataSource<NoticeDataResponse>(){
                override suspend fun createCall(): Response<NoticeDataResponse> {

                    return noticeApi.getNotice(request.title,request.page_number,request.page_size, request.sort_by)
                }
            }
            request.networkRequest()
            emit(request.asLiveData().value!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
