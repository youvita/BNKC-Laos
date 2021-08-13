package com.mobile.bnkcl.data.repository.intro

import com.bnkc.library.data.type.Resource
import com.bnkc.library.data.network.RemoteDataSource
import com.mobile.bnkcl.data.response.common.MGDataResponse
import com.mobile.bnkcl.data.api.common.MGApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class MGRepo(private val mgApi: MGApi) {

    fun getMGData(): Flow<Resource<MGDataResponse>> = flow {
        delay(1000)
        try {
            val request = object: RemoteDataSource<MGDataResponse>(){
                override suspend fun createCall(): Response<MGDataResponse> {
                    return mgApi.getMGData("AOS")
                }
            }
            request.networkRequest()
            emit(request.asLiveData().value!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}