package com.mobile.bnkcl.di

import com.bnkc.library.prefer.CredentialSharedPrefer
import com.mobile.bnkcl.app.AppBuild
import com.mobile.bnkcl.data.api.CommentApi
import com.mobile.bnkcl.data.api.common.MGApi
import com.mobile.bnkcl.data.api.UserApi
import com.mobile.bnkcl.data.api.auth.AuthAPI
import com.mobile.bnkcl.data.api.dashboard.DashboardApi
import com.mobile.bnkcl.data.api.otp.OTPApi
import com.mobile.bnkcl.data.api.signup.SignUpApi
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

//    @Singleton
//    @Provides
//    fun provideUserService(retrofit: Retrofit.Builder, credentialSharedPrefer: CredentialSharedPrefer): UserApi {
//        return retrofit
//                .baseUrl(AppBuild.BASE_URL)
//                .build()
//                .create(UserApi::class.java)
//    }

    @Singleton
    @Provides
    fun providePreSignUpService(retrofit: Retrofit.Builder): SignUpApi{
        return retrofit
            .baseUrl(AppBuild.BASE_URL)
            .build()
            .create(SignUpApi::class.java)
    }

//    @Singleton
//    @Provides
//    fun provideOTPService(retrofit: Retrofit.Builder): OTPApi{
//        return retrofit
//            .baseUrl(AppBuild.BASE_URL)
//            .build()
//            .create(OTPApi::class.java)
//    }
//
//    @Singleton
//    @Provides
//    fun provideAuthService(retrofit: Retrofit.Builder): AuthAPI{
//        return retrofit
//            .baseUrl(AppBuild.BASE_URL)
//            .build()
//            .create(AuthAPI::class.java)
//    }

    @Singleton
    @Provides
    fun provideDashboardService(retrofit: Retrofit.Builder): DashboardApi{
        return retrofit
            .baseUrl(AppBuild.BASE_URL)
            .build()
            .create(DashboardApi::class.java)
    }
}