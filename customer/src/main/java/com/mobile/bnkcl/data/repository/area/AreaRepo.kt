package com.mobile.bnkcl.data.repository.area

import android.content.Context
import com.bnkc.library.data.network.RemoteDataSource
import com.bnkc.library.data.type.Resource
import com.bnkc.sourcemodule.app.RetrofitBuilder
import com.mobile.bnkcl.data.api.area.AreaApi
import com.mobile.bnkcl.data.api.notice.NoticeApi
import com.mobile.bnkcl.data.response.area.CapitalData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import retrofit2.Response
import java.lang.Exception

class AreaRepo(context: Context, okHttpClient: OkHttpClient){
    private val areaApi: AreaApi by lazy {
        RetrofitBuilder(context, okHttpClient).getRetrofit().create(AreaApi::class.java)
    }
    //get all capital data
    fun getCapitalData() : Flow<Resource<CapitalData>> = flow{
        try {
            val request = object : RemoteDataSource<CapitalData>(){
                override suspend fun createCall(): Response<CapitalData> {
                    return areaApi.getCapital("STATE")
                }
            }
            request.networkRequest()
            emit(request.asLiveData().value!!)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}