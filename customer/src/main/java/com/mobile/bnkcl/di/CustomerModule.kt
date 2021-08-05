package com.mobile.bnkcl.di

import com.mobile.bnkcl.app.Constants
import com.mobile.bnkcl.data.api.CommentApi
import com.mobile.bnkcl.data.api.MGApi
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

}