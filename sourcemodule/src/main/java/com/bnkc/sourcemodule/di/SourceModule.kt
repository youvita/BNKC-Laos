package com.bnkc.sourcemodule.di

import com.bnkc.sourcemodule.dialog.DatePickerDialog
import com.bnkc.sourcemodule.dialog.LoadingDialog
import com.bnkc.sourcemodule.dialog.SystemDialog
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

    @Singleton
    @Provides
    fun provideDatePickerDialog() = DatePickerDialog()

    @Singleton
    @Provides
    fun provideSystemDialog() = SystemDialog()

}