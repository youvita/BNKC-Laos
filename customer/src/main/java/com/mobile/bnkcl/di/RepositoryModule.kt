package com.mobile.bnkcl.di

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
    fun provideOTPRepo(@AuthInterceptorOkHttpClient okHttpClient: OkHttpClient): OTPRepo {
        return OTPRepo(okHttpClient)
    }

    @Singleton
    @Provides
    fun provideAuthRepo(@AuthInterceptorOkHttpClient okHttpClient: OkHttpClient): AuthRepo {
        return AuthRepo(okHttpClient)
    }

    @Singleton
    @Provides
    fun provideUserRepo(@AuthInterceptorOkHttpClient okHttpClient: OkHttpClient): UserRepo {
        return UserRepo(okHttpClient)
    }

    @Singleton
    @Provides
    fun provideAlarmRepo(@AuthInterceptorOkHttpClient okHttpClient: OkHttpClient): AlarmRepo {
        return AlarmRepo(okHttpClient)
    }

    @Singleton
    @Provides
    fun provideDashboardRepo(@AuthInterceptorOkHttpClient okHttpClient: OkHttpClient): DashboardRepo {
        return DashboardRepo(okHttpClient)
    }

    @Singleton
    @Provides
    fun provideNoticeRepo(@AuthInterceptorOkHttpClient okHttpClient: OkHttpClient): NoticeRepo{
        return NoticeRepo(okHttpClient)
    }

    @Singleton
    @Provides
    fun provideFindOfficeRepo(@AuthInterceptorOkHttpClient okHttpClient: OkHttpClient): FindOfficeRepo {
        return FindOfficeRepo(okHttpClient)
    }

    @Singleton
    @Provides
    fun provideAreaRepo(@AuthInterceptorOkHttpClient okHttpClient: OkHttpClient): AreaRepo{
        return AreaRepo(okHttpClient)
    }

    @Singleton
    @Provides
    fun provideLeaseRepo(@AuthInterceptorOkHttpClient okHttpClient: OkHttpClient): LeaseRepo {
        return LeaseRepo(okHttpClient)
    }

    @Singleton
    @Provides
    fun provideClaimRepo(@AuthInterceptorOkHttpClient okHttpClient: OkHttpClient): ClaimRepo{
        return ClaimRepo(okHttpClient)
    }

    @Singleton
    @Provides
    fun provideClaimDetailRepo(@AuthInterceptorOkHttpClient okHttpClient: OkHttpClient) :  ClaimDetailRepo{
        return ClaimDetailRepo(okHttpClient)
    }

    @Singleton
    @Provides
    fun provideFaqsDataRepo(@AuthInterceptorOkHttpClient okHttpClient: OkHttpClient) :  FaqsRepo{
        return FaqsRepo(okHttpClient)
    }

    @Singleton
    @Provides
    fun provideEditAccountInfoRepo(@AuthInterceptorOkHttpClient okHttpClient: OkHttpClient): EditAccountInfoRepo {
        return EditAccountInfoRepo(okHttpClient)
    }

    @Singleton
    @Provides
    fun provideCodesRepo(@AuthInterceptorOkHttpClient okHttpClient: OkHttpClient): CodesRepo {
        return CodesRepo(okHttpClient)
    }

}