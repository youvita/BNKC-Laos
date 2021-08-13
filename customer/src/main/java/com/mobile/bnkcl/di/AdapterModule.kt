package com.mobile.bnkcl.di

import com.mobile.bnkcl.ui.adapter.CommentAdapter
import com.mobile.bnkcl.ui.adapter.TotalLeaseScheduleAdapter
import com.mobile.bnkcl.ui.adapter.TransactionHistoryAdapter
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
    fun provideCommentAdapter() = CommentAdapter()

    @Singleton
    @Provides
    fun provideTransactionHistoryAdapter() = TransactionHistoryAdapter()

    @Singleton
    @Provides
    fun provideTotalLeaseScheduleAdapter() = TotalLeaseScheduleAdapter()

}