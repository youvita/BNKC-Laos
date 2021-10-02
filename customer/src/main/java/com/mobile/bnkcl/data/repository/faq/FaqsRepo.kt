package com.mobile.bnkcl.data.repository.faq

import com.bnkc.library.data.network.RemoteDataSource
import com.bnkc.library.data.type.Resource
import com.bnkc.sourcemodule.app.RetrofitBuilder
import com.mobile.bnkcl.data.api.faq.FaqsApi
import com.mobile.bnkcl.data.request.faq.FaqReq
import com.mobile.bnkcl.data.response.faq.FaqRes
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import retrofit2.Response
import javax.inject.Inject

class FaqsRepo @Inject constructor(okHttpClient: OkHttpClient) {

    private val faqsApi : FaqsApi by lazy {
        RetrofitBuilder(okHttpClient).getRetrofit().create(FaqsApi::class.java)
    }

    fun getFaqData(request: FaqReq): Flow<Resource<FaqRes>> = flow {
        try {
            delay(1000)
            val request = object: RemoteDataSource<FaqRes>(){
                override suspend fun createCall(): Response<FaqRes> {
                    return faqsApi.getFaqData(request.page_number, request.page_size, request.sort_by)
                }
            }
            request.networkRequest()
            emit(request.asLiveData().value!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}