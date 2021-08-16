package com.mobile.bnkcl.data.repository.user

import android.content.Context
import com.bnkc.library.data.network.RemoteDataSource
import com.bnkc.library.data.type.Resource
import com.bnkc.sourcemodule.app.RetrofitBuilder
import com.mobile.bnkcl.data.api.UserApi
import com.mobile.bnkcl.data.response.user.ProfileData
import com.mobile.bnkcl.data.response.user.SettingData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import retrofit2.Response

class UserRepo(context: Context, okHttpClient: OkHttpClient) {

    private val userApi: UserApi by lazy { RetrofitBuilder(context, okHttpClient).getRetrofit().create(UserApi::class.java) }

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

    fun updateUserSetting(body: SettingData): Flow<Resource<SettingData>> = flow {
        delay(1000)
        try {
            val request = object : RemoteDataSource<SettingData>(){
                override suspend fun createCall(): Response<SettingData> {
                    return userApi.updateUserSetting(body)
                }
            }
            request.networkRequest()
            emit(request.asLiveData().value!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}