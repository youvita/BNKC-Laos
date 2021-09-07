package com.mobile.bnkcl.data.repository.user

import android.content.Context
import com.bnkc.library.data.network.RemoteDataSource
import com.bnkc.library.data.type.Resource
import com.bnkc.sourcemodule.app.RetrofitBuilder
import com.mobile.bnkcl.data.api.user.UserApi
import com.mobile.bnkcl.data.response.user.ProfileData
import com.mobile.bnkcl.data.response.user.SettingData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import retrofit2.Response
import javax.inject.Inject

class UserRepo @Inject constructor(context: Context, okHttpClient: OkHttpClient) {

    private val userApi: UserApi by lazy {
        RetrofitBuilder(context, okHttpClient).getRetrofit().create(UserApi::class.java)
    }

    fun getProfile(): Flow<Resource<ProfileData>> = flow {
        delay(1000)
        try {
            val request = object: RemoteDataSource<ProfileData>(){
                override suspend fun createCall(): Response<ProfileData> {
                    return userApi.getProfile()
                }
            }
            request.networkRequest()
            emit(request.asLiveData().value!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateUserSetting(body: SettingData): Flow<Resource<Unit>> = flow {
        delay(1000)
        try {
            val request = object : RemoteDataSource<Unit>(){
                override suspend fun createCall(): Response<Unit> {
                    return userApi.updateUserSetting(body)
                }
            }
            request.networkRequest()
            emit(request.asLiveData().value!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun logout(): Flow<Resource<Unit>> = flow {
        delay(1000)
        try {
            val request = object: RemoteDataSource<Unit>(){
                override suspend fun createCall(): Response<Unit> {
                    return userApi.logout()
                }
            }
            request.networkRequest()
            emit(request.asLiveData().value!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}