/**
 * KOSIGN (Cambodia) Investment Co., Ltd.
 * @author chan youvita
 * @since 2021.08.04
 */
package com.bnkc.library.di

import android.content.Context
import com.bnkc.library.R
import com.bnkc.library.custom.cardview.CardOffsetDecoration
import com.bnkc.library.custom.cardview.CardRecyclerView
import com.bnkc.library.permission.AppPermissionsFactory
import com.bnkc.library.prefer.CredentialSharedPrefer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideCredentialSharedPreference(@ApplicationContext context: Context) = CredentialSharedPrefer.getInstance(context)


    @Singleton
    @Provides
    fun provideAppPermissionFactory() = AppPermissionsFactory()

    @Singleton
    @Provides
    fun provideCardRecyclerView() = CardRecyclerView()

    @Singleton
    @Provides
    fun provideCardItemOffsetDecoration(@ApplicationContext context: Context): CardOffsetDecoration {
        val cardSpacing = context.resources.getDimensionPixelOffset(R.dimen.default_spacing_5dp)
        return CardOffsetDecoration(cardSpacing)
    }

}