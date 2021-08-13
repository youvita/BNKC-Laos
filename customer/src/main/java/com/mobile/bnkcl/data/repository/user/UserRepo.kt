package com.mobile.bnkcl.data.repository.user

import com.bnkc.library.data.network.RemoteDataSource
import com.bnkc.library.data.type.Resource
import com.mobile.bnkcl.data.api.UserApi
import com.mobile.bnkcl.data.response.user.ProfileData
import com.mobile.bnkcl.data.response.user.SettingData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class UserRepo(private val userApi: UserApi) {

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
        try {
            delay(1000)
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