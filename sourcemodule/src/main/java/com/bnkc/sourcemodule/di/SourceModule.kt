package com.bnkc.sourcemodule.di

import com.bnkc.sourcemodule.dialog.*
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

    @Singleton
    @Provides
    fun provideConfirmDialog() = ConfirmDialog()

    @Singleton
    @Provides
    fun provideListChoiceDialog() = ListChoiceDialog()

    @Singleton
    @Provides
    fun provideTwoButtonDialog() = TwoButtonDialog()

    @Singleton
    @Provides
    fun providePhotoSettingMenu() = PhotoSettingMenu()

}