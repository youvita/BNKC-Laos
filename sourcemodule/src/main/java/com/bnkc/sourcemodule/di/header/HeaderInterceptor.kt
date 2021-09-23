package com.bnkc.sourcemodule.di.header


import com.bnkc.library.data.type.RunTimeDataStore
import com.bnkc.library.prefer.CredentialSharedPrefer
import com.bnkc.sourcemodule.BuildConfig
import com.bnkc.sourcemodule.app.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

const val AUTHORIZATION = "Authorization"
const val BEARER = "Bearer"
const val ACCEPT_LANGUAGE = "Accept-Language"
const val X_APP_VERSION = "X-App-Version"

@Module
@InstallIn(SingletonComponent::class)
class HeaderInterceptor {

    @Provides
    @Singleton
    fun provideHeaderInterceptor(credentialSharedPrefer: CredentialSharedPrefer): Interceptor {
        return Interceptor(fun(chain: Interceptor.Chain): Response {
            val tokenBearer = "$BEARER ${RunTimeDataStore.LoginToken.value}"
            val languageCode = credentialSharedPrefer.getPrefer(Constants.LANGUAGE).orEmpty()

            val request = chain.request()
                    .newBuilder()
                    .header(ACCEPT_LANGUAGE, if (languageCode.isNullOrEmpty()) "lo" else languageCode)
                    .header(X_APP_VERSION, "")
            when {
                !RunTimeDataStore.LoginToken.value.isNullOrEmpty() -> {
                    request.header(AUTHORIZATION, tokenBearer)
                }
            }
            return chain.proceed(request.build())
        })
    }

    @Provides
    @Singleton
    fun provideLoggingHttp(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    @AuthInterceptorOkHttpClient
    @Provides
    fun provideAuthInterceptorOkHttpClient(
            logging: HttpLoggingInterceptor,
            header: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(header)
                .addInterceptor(logging)
                .retryOnConnectionFailure(true)
                .readTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
                .build()
    }

    companion object {
        private const val TIMEOUT = 30
    }
}