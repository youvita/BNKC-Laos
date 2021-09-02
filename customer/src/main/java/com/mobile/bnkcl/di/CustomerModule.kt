package com.mobile.bnkcl.di

import com.mobile.bnkcl.app.AppBuild
import com.mobile.bnkcl.data.api.code.CodesApi
import com.mobile.bnkcl.data.api.common.MGApi
import com.mobile.bnkcl.data.api.dashboard.DashboardApi
import com.mobile.bnkcl.data.api.lease.LeaseApi
import com.mobile.bnkcl.data.api.signup.SignUpApi
import com.mobile.bnkcl.data.api.user.EditAccountInfoApi
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

    @Singleton
    @Provides
    fun providePreSignUpService(retrofit: Retrofit.Builder): SignUpApi{
        return retrofit
            .baseUrl(AppBuild.BASE_URL)
            .build()
            .create(SignUpApi::class.java)
    }

    @Singleton
    @Provides
    fun provideDashboardService(retrofit: Retrofit.Builder): DashboardApi{
        return retrofit
            .baseUrl(AppBuild.BASE_URL)
            .build()
            .create(DashboardApi::class.java)
    }

    @Singleton
    @Provides
    fun provideLeaseService(retrofit: Retrofit.Builder): LeaseApi{
        return retrofit
            .baseUrl(AppBuild.BASE_URL)
            .build()
            .create(LeaseApi::class.java)
    }

    @Singleton
    @Provides
    fun provideEditAccountInfoService(retrofit: Retrofit.Builder): EditAccountInfoApi{
        return retrofit
            .baseUrl(AppBuild.BASE_URL)
            .build()
            .create(EditAccountInfoApi::class.java)
    }

    @Singleton
    @Provides
    fun provideCodesService(retrofit: Retrofit.Builder): CodesApi{
        return retrofit
            .baseUrl(AppBuild.BASE_URL)
            .build()
            .create(CodesApi::class.java)
    }
}