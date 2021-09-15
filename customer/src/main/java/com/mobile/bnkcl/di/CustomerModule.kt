package com.mobile.bnkcl.di

import com.mobile.bnkcl.app.AppBuild
import com.mobile.bnkcl.data.api.common.MGApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CustomerModule {

    @Singleton
    @Provides
    fun provideMGService(retrofit: Retrofit.Builder): MGApi {
        return retrofit
            .baseUrl(AppBuild.MG_URL)
            .build()
            .create(MGApi::class.java)
    }
}