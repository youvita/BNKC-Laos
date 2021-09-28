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
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.net.CookieManager
import java.net.CookiePolicy
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
                    .header(ACCEPT_LANGUAGE, if (languageCode.isEmpty()) "lo" else languageCode)
                    .header(X_APP_VERSION, "")

            // auth when endpoint required
            if (RunTimeDataStore.LoginToken.value.isNotEmpty()){
                request.header(AUTHORIZATION, tokenBearer)
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

    @Provides
    @Singleton
    fun provideCookieJar(): CookieManager {
        val cookieManager = CookieManager()
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER)
        return cookieManager
    }

    @AuthInterceptorOkHttpClient
    @Provides
    fun provideAuthInterceptorOkHttpClient(
            logging: HttpLoggingInterceptor,
            header: Interceptor,
            cookie: CookieManager
    ): OkHttpClient {
        return OkHttpClient.Builder()
                .cookieJar(JavaNetCookieJar(cookie))
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