package com.mobile.bnkcl.data.repository.intro

import com.bnkc.library.data.Resource
import com.bnkc.library.network.RemoteDataSource
import com.mobile.bnkcl.data.MGData
import com.mobile.bnkcl.data.api.MGApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class MGRepo(private val mgApi: MGApi) {

    fun getMGData(): Flow<Resource<MGData>> = flow {
        delay(1000)
        try {
            val request = object: RemoteDataSource<MGData>(){
                override suspend fun createCall(): Response<MGData> {
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