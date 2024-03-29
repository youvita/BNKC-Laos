package com.mobile.bnkcl.di

import android.content.Context
import com.bnkc.sourcemodule.di.header.AuthInterceptorOkHttpClient
import com.mobile.bnkcl.data.api.common.MGApi
import com.mobile.bnkcl.data.repository.alarm.AlarmRepo
import com.mobile.bnkcl.data.repository.area.AreaRepo
import com.mobile.bnkcl.data.repository.auth.AuthRepo
import com.mobile.bnkcl.data.repository.code.CodesRepo
import com.mobile.bnkcl.data.repository.cscenter.ClaimDetailRepo
import com.mobile.bnkcl.data.repository.cscenter.ClaimRepo
import com.mobile.bnkcl.data.repository.dashboard.DashboardRepo
import com.mobile.bnkcl.data.repository.faq.FaqsRepo
import com.mobile.bnkcl.data.repository.findoffice.FindOfficeRepo
import com.mobile.bnkcl.data.repository.intro.MGRepo
import com.mobile.bnkcl.data.repository.lease.LeaseRepo
import com.mobile.bnkcl.data.repository.notice.NoticeRepo
import com.mobile.bnkcl.data.repository.otp.OTPRepo
import com.mobile.bnkcl.data.repository.user.EditAccountInfoRepo
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
    fun provideUserRepo(@ApplicationContext context: Context, @AuthInterceptorOkHttpClient okHttpClient: OkHttpClient): UserRepo {
        return UserRepo(context, okHttpClient)
    }

    @Singleton
    @Provides
    fun provideAlarmRepo(@ApplicationContext context: Context, @AuthInterceptorOkHttpClient okHttpClient: OkHttpClient): AlarmRepo {
        return AlarmRepo(context, okHttpClient)
    }

    @Singleton
    @Provides
    fun provideDashboardRepo(@ApplicationContext context: Context, @AuthInterceptorOkHttpClient okHttpClient: OkHttpClient): DashboardRepo {
        return DashboardRepo(context, okHttpClient)
    }

    @Singleton
    @Provides
    fun provideNoticeRepo(@ApplicationContext context: Context, @AuthInterceptorOkHttpClient okHttpClient: OkHttpClient): NoticeRepo{
        return NoticeRepo(context,okHttpClient)
    }

    @Singleton
    @Provides
    fun provideFindOfficeRepo(@ApplicationContext context: Context, @AuthInterceptorOkHttpClient okHttpClient: OkHttpClient): FindOfficeRepo {
        return FindOfficeRepo(context,okHttpClient)
    }

    @Singleton
    @Provides
    fun provideAreaRepo(@ApplicationContext context: Context, @AuthInterceptorOkHttpClient okHttpClient: OkHttpClient): AreaRepo{
        return AreaRepo(context,okHttpClient)
    }

    @Singleton
    @Provides
    fun provideLeaseRepo(@ApplicationContext context: Context, @AuthInterceptorOkHttpClient okHttpClient: OkHttpClient): LeaseRepo {
        return LeaseRepo(context, okHttpClient)
    }

    @Singleton
    @Provides
    fun provideClaimRepo(@ApplicationContext context: Context, @AuthInterceptorOkHttpClient okHttpClient: OkHttpClient): ClaimRepo{
        return ClaimRepo(context, okHttpClient)
    }

    @Singleton
    @Provides
    fun provideClaimDetailRepo(@ApplicationContext context: Context, @AuthInterceptorOkHttpClient okHttpClient: OkHttpClient) :  ClaimDetailRepo{
        return ClaimDetailRepo(context,okHttpClient)
    }

    @Singleton
    @Provides
    fun provideFaqsDataRepo(@ApplicationContext context: Context, @AuthInterceptorOkHttpClient okHttpClient: OkHttpClient) :  FaqsRepo{
        return FaqsRepo(context,okHttpClient)
    }

    @Singleton
    @Provides
    fun provideEditAccountInfoRepo(@ApplicationContext context: Context, @AuthInterceptorOkHttpClient okHttpClient: OkHttpClient): EditAccountInfoRepo {
        return EditAccountInfoRepo(context, okHttpClient)
    }

    @Singleton
    @Provides
    fun provideCodesRepo(@ApplicationContext context: Context, @AuthInterceptorOkHttpClient okHttpClient: OkHttpClient): CodesRepo {
        return CodesRepo(context, okHttpClient)
    }


}