package com.mobile.bnkcl.di

import com.mobile.bnkcl.ui.adapter.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AdapterModule {

    @Singleton
    @Provides
    fun provideTransactionHistoryAdapter() = TransactionHistoryAdapter()

    @Singleton
    @Provides
    fun provideTotalLeaseScheduleAdapter() = TotalLeaseScheduleAdapter()

    @Singleton
    @Provides
    fun provideNoticeAdapter() = NoticeAdapter()

    @Singleton
    @Provides
    fun provideLeaseRequestProcessAdapter() = LeaseRequestProcessAdapter()

    @Singleton
    @Provides
    fun provideAlarmAdapter() = AlarmAdapter()

    @Singleton
    @Provides
    fun provideBannerAdapter() = BannerAdapter()
}