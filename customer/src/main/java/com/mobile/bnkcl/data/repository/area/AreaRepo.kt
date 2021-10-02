package com.mobile.bnkcl.data.repository.area

import com.bnkc.library.data.network.RemoteDataSource
import com.bnkc.library.data.type.Resource
import com.bnkc.sourcemodule.app.RetrofitBuilder
import com.mobile.bnkcl.data.api.area.AreaApi
import com.mobile.bnkcl.data.request.area.AddressData
import com.mobile.bnkcl.data.response.area.AreaRes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import retrofit2.Response

class AreaRepo(okHttpClient: OkHttpClient){
    private val areaApi: AreaApi by lazy {
        RetrofitBuilder(okHttpClient).getRetrofit().create(AreaApi::class.java)
    }
    //get all capital data
    fun getCapitalData() : Flow<Resource<AreaRes>> = flow{
        try {
            val request = object : RemoteDataSource<AreaRes>(){
                override suspend fun createCall(): Response<AreaRes> {
                    return areaApi.getCapital("STATE")
                }
            }
            request.networkRequest()
            emit(request.asLiveData().value!!)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun getAddressData(addressData: AddressData) : Flow<Resource<AreaRes>> = flow {
        try {
            val request = object : RemoteDataSource<AreaRes>(){
                override suspend fun createCall(): Response<AreaRes> {
                    return areaApi.getAddress(addressData.category!!, addressData.parent_id!!)
                }
            }
            request.networkRequest()
            emit(request.asLiveData().value!!)

        }catch (e: Exception){
            e.printStackTrace()
        }
    }


}