package com.bnkc.sourcemodule.di

import com.bnkc.sourcemodule.dialog.LoadingDialog
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SourceModule {

    @Singleton
    @Provides
    fun provideLoadingDialog() = LoadingDialog()
}