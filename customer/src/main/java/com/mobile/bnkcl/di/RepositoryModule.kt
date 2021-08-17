package com.mobile.bnkcl.di

import android.content.Context
import com.bnkc.sourcemodule.di.header.AuthInterceptorOkHttpClient
import com.mobile.bnkcl.data.api.CommentApi
import com.mobile.bnkcl.data.api.common.MGApi
import com.mobile.bnkcl.data.api.UserApi
import com.mobile.bnkcl.data.api.auth.AuthAPI
import com.mobile.bnkcl.data.api.otp.OTPApi
import com.mobile.bnkcl.data.api.signup.SignUpApi
import com.mobile.bnkcl.data.repository.auth.AuthRepo
import com.mobile.bnkcl.data.repository.comment.CommentRepo
import com.mobile.bnkcl.data.repository.login.LoginRepo
import com.mobile.bnkcl.data.repository.intro.MGRepo
import com.mobile.bnkcl.data.repository.otp.OTPRepo
import com.mobile.bnkcl.data.repository.signup.SignUpRepo
import com.mobile.bnkcl.data.repository.user.UserRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideCommentRepo(commentApi: CommentApi): CommentRepo {
        return CommentRepo(commentApi)
    }

    @Singleton
    @Provides
    fun provideMGRepo(mgApi: MGApi): MGRepo {
        return MGRepo(mgApi)
    }

    @Singleton
    @Provides
    fun provideOTPRepo(@ApplicationContext context: Context, @AuthInterceptorOkHttpClient okHttpClient: OkHttpClient): OTPRepo {
        return OTPRepo(context, okHttpClient)
    }

    @Singleton
    @Provides
    fun provideAuthRepo(@ApplicationContext context: Context, @AuthInterceptorOkHttpClient okHttpClient: OkHttpClient): AuthRepo {
        return AuthRepo(context, okHttpClient)
    }

    @Singleton
    @Provides
    fun provideLoginRepo() : LoginRepo {
        return LoginRepo()
    }

    @Singleton
    @Provides
    fun provideUserRepo(@ApplicationContext context: Context, @AuthInterceptorOkHttpClient okHttpClient: OkHttpClient): UserRepo {
        return UserRepo(context, okHttpClient)
    }

    @Singleton
    @Provides
    fun providePreSignUpRepo(signUpApi: SignUpApi): SignUpRepo{
        return SignUpRepo(signUpApi)
    }
}