package com.bnkc.sourcemodule.app

import android.content.Context
import com.bnkc.library.prefer.CredentialSharedPrefer
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * instance retrofit to generate api service.
 */
class RetrofitBuilder(private val context: Context, private val okHttpClient: OkHttpClient) {

    fun getRetrofit(): Retrofit {
        val credentialSharedPrefer = CredentialSharedPrefer.getInstance(context)
        val baseUrl = "${credentialSharedPrefer.getPrefer(Constants.KEY_START_URL)}"
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

}