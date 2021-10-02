package com.mobile.bnkcl.data.repository.user

import com.bnkc.library.data.network.RemoteDataSource
import com.bnkc.library.data.type.Resource
import com.bnkc.sourcemodule.app.RetrofitBuilder
import com.mobile.bnkcl.data.api.user.EditAccountInfoApi
import com.mobile.bnkcl.data.response.user.EditAccountInfoData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import retrofit2.Response
import javax.inject.Inject

class EditAccountInfoRepo @Inject constructor(okHttpClient: OkHttpClient) {

    private val editAccountInfoApi: EditAccountInfoApi by lazy {
        RetrofitBuilder(okHttpClient).getRetrofit().create(EditAccountInfoApi::class.java)
    }

    fun editAccountInfo(editAccountInfoData: EditAccountInfoData): Flow<Resource<Unit>> = flow {
        delay(1000)
        try {
            val request = object : RemoteDataSource<Unit>(){
                override suspend fun createCall(): Response<Unit> {
                    return editAccountInfoApi.editAccountInfo(editAccountInfoData)
                }
            }
            request.networkRequest()
            emit(request.asLiveData().value!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun uploadProfile(multipartBody: MultipartBody): Flow<Resource<Unit>> = flow {
        delay(1000)
        try {
            val request = object : RemoteDataSource<Unit>(){
                override suspend fun createCall(): Response<Unit> {
                    return editAccountInfoApi.uploadProfile(multipartBody)
                }
            }
            request.networkRequest()
            emit(request.asLiveData().value!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}