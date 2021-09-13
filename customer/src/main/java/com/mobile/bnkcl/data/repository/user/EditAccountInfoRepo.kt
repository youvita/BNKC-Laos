package com.mobile.bnkcl.data.repository.user

import android.content.Context
import com.bnkc.library.data.network.RemoteDataSource
import com.bnkc.library.data.type.Resource
import com.bnkc.sourcemodule.app.RetrofitBuilder
import com.mobile.bnkcl.data.api.user.EditAccountInfoApi
import com.mobile.bnkcl.data.response.user.EditAccountInfoData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import retrofit2.Response
import javax.inject.Inject

class EditAccountInfoRepo @Inject constructor(context: Context, okHttpClient: OkHttpClient) {

    private val editAccountInfoApi: EditAccountInfoApi by lazy {
        RetrofitBuilder(context, okHttpClient).getRetrofit().create(EditAccountInfoApi::class.java)
    }

    fun editAccountInfo(editAccountInfoData: EditAccountInfoData): Flow<Resource<Boolean>> = flow {
        delay(1000)
        try {
            val request = object : RemoteDataSource<Boolean>(){
                override suspend fun createCall(): Response<Boolean> {
                    return editAccountInfoApi.editAccountInfo(editAccountInfoData)
                }
            }
            request.networkRequest()
            emit(request.asLiveData().value!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}