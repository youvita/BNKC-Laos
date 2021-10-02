package com.mobile.bnkcl.data.repository.cscenter

import com.bnkc.library.data.network.RemoteDataSource
import com.bnkc.library.data.type.Resource
import com.bnkc.sourcemodule.app.RetrofitBuilder
import com.mobile.bnkcl.data.api.cscenter.ClaimDetailApi
import com.mobile.bnkcl.data.request.cscenter.ClaimDetailReq
import com.mobile.bnkcl.data.response.cscenter.ClaimDetailRes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import retrofit2.Response

class ClaimDetailRepo(okHttpClient: OkHttpClient) {

    private val claimApi : ClaimDetailApi by lazy {
        RetrofitBuilder(okHttpClient).getRetrofit().create(ClaimDetailApi::class.java)
    }

    fun getClaimDetailData(request : ClaimDetailReq) : Flow<Resource<ClaimDetailRes>> = flow{
        try {
            val request = object: RemoteDataSource<ClaimDetailRes>(){
                override suspend fun createCall(): Response<ClaimDetailRes> {
                    return claimApi.getClaimDetailData(request.claim_id)
                }
            }
            request.networkRequest()
            emit(request.asLiveData().value!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
