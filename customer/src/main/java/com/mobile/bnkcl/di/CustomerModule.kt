package com.mobile.bnkcl.di

import com.bnkc.library.prefer.CredentialSharedPrefer
import com.bnkc.sourcemodule.app.Constants
import com.mobile.bnkcl.app.AppBuild
import com.mobile.bnkcl.data.api.CommentApi
import com.mobile.bnkcl.data.api.MGApi
import com.mobile.bnkcl.data.api.UserApi
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
            .baseUrl(AppBuild.BASE_URL)
            .build()
            .create(CommentApi::class.java)

    }

    @Singleton
    @Provides
    fun provideMGService(retrofit: Retrofit.Builder): MGApi {
        return retrofit
            .baseUrl(AppBuild.MG_URL)
            .build()
            .create(MGApi::class.java)
    }

    @Singleton
    @Provides
    fun provideUserService(retrofit: Retrofit.Builder, credentialSharedPrefer: CredentialSharedPrefer): UserApi {
        return retrofit
                .baseUrl(credentialSharedPrefer.getPrefer(Constants.KEY_START_URL)!!)
                .build()
                .create(UserApi::class.java)
    }

}