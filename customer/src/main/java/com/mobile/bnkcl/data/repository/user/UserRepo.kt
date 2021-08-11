package com.mobile.bnkcl.data.repository.user

import com.bnkc.library.data.network.RemoteDataSource
import com.bnkc.library.data.type.Resource
import com.mobile.bnkcl.data.api.UserApi
import com.mobile.bnkcl.data.response.user.ProfileData
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
}