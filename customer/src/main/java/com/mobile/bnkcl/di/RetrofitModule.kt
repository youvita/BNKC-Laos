package com.mobile.bnkcl.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mobile.bnkcl.app.Constants
import com.mobile.bnkcl.data.api.CommentApi
import com.mobile.bnkcl.data.api.MGApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson {
        return GsonBuilder().create()
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson, @AuthInterceptorOkHttpClient okHttpClient: OkHttpClient): Retrofit.Builder {
        return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
    }

    @Singleton
    @Provides
    fun provideCommentService(retrofit: Retrofit.Builder): CommentApi {
        return retrofit
            .baseUrl(Constants.BASE_URL)
            .build()
            .create(CommentApi::class.java)

    }

    @Singleton
    @Provides
    fun provideMGService(retrofit: Retrofit.Builder): MGApi {
        return retrofit
            .baseUrl(Constants.MG_URL)
            .build()
            .create(MGApi::class.java)
    }

    /* TODO: add more api service here */

}
