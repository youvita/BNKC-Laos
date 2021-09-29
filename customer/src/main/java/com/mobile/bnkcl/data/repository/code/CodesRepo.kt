package com.mobile.bnkcl.data.repository.code

import android.content.Context
import com.bnkc.library.data.network.RemoteDataSource
import com.bnkc.library.data.type.Resource
import com.bnkc.sourcemodule.app.RetrofitBuilder
import com.mobile.bnkcl.data.api.code.CodesApi
import com.mobile.bnkcl.data.api.dashboard.DashboardApi
import com.mobile.bnkcl.data.response.code.CodesResponse
import com.mobile.bnkcl.data.response.code.ProductResponse
import com.mobile.bnkcl.data.response.dashboard.DashboardResponse
import com.mobile.bnkcl.data.response.dashboard.LeaseApplicationResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import retrofit2.Response
import javax.inject.Inject

class CodesRepo @Inject constructor(context: Context, okHttpClient: OkHttpClient) {

    private val codesApi: CodesApi by lazy {
        RetrofitBuilder(context, okHttpClient).getRetrofit().create(CodesApi::class.java)
    }

    fun getCodes(group_id : String): Flow<Resource<CodesResponse>> = flow {
        delay(1000)
        try {
            val request = object: RemoteDataSource<CodesResponse>(){
                override suspend fun createCall(): Response<CodesResponse> {
                    return codesApi.getCodes(group_id)
                }
            }
            request.networkRequest()
            emit(request.asLiveData().value!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getProductCodes(group_id : String): Flow<Resource<ProductResponse>> = flow {
        delay(1000)
        try {
            val request = object: RemoteDataSource<ProductResponse>(){
                override suspend fun createCall(): Response<ProductResponse> {
                    return codesApi.getProductCodes(group_id)
                }
            }
            request.networkRequest()
            emit(request.asLiveData().value!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getProductFilterCodes(cat :String,id : String): Flow<Resource<ProductResponse>> = flow {
        delay(1000)
        try {
            val request = object: RemoteDataSource<ProductResponse>(){
                override suspend fun createCall(): Response<ProductResponse> {
                    return codesApi.getProductCodes(cat, id)
                }
            }
            request.networkRequest()
            emit(request.asLiveData().value!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}