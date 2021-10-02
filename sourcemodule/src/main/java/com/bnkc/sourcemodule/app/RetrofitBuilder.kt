package com.bnkc.sourcemodule.app

import com.bnkc.library.data.type.RunTimeDataStore
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * instance retrofit to generate api service.
 */
class RetrofitBuilder(private val okHttpClient: OkHttpClient) {

    fun getRetrofit(): Retrofit {
        val baseUrl = RunTimeDataStore.BaseUrl.value
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

}