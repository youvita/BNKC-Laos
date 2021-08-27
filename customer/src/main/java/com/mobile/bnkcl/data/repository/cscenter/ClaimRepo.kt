package com.mobile.bnkcl.data.repository.cscenter

import android.content.Context
import com.bnkc.library.data.network.RemoteDataSource
import com.bnkc.library.data.type.Resource
import com.bnkc.sourcemodule.app.RetrofitBuilder
import com.mobile.bnkcl.data.api.cscenter.ClaimApi
import com.mobile.bnkcl.data.request.cscenter.ClaimDataRequest
import com.mobile.bnkcl.data.request.cscenter.ClaimReq
import com.mobile.bnkcl.data.response.cscenter.ClaimDataResponse
import com.mobile.bnkcl.data.response.cscenter.ClaimRes
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import retrofit2.Response

class ClaimRepo(context: Context, okHttpClient: OkHttpClient) {

    private val claimApi : ClaimApi by lazy {
        RetrofitBuilder(context, okHttpClient).getRetrofit().create(ClaimApi::class.java)
    }

    fun getClaimData(request: ClaimDataRequest): Flow<Resource<ClaimDataResponse>> = flow {
        try {
            val request = object: RemoteDataSource<ClaimDataResponse>(){
                override suspend fun createCall(): Response<ClaimDataResponse> {
                    return claimApi.getClaimData(request.page_number, request.page_size, request.sort_by)
                }
            }
            request.networkRequest()
            emit(request.asLiveData().value!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getClaim(request: ClaimReq) : Flow<Resource<ClaimRes>> = flow{
        try {
            val request = object: RemoteDataSource<ClaimRes>(){
                override suspend fun createCall(): Response<ClaimRes> {
                    return claimApi.getClaim(request)
                }
            }
            request.networkRequest()
            emit(request.asLiveData().value!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}