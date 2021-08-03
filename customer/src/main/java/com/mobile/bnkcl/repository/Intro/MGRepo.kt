package com.mobile.bnkcl.repository.Intro

import com.bnkc.library.network.RemoteDataSource
import com.mobile.bnkcl.data.MGData
import com.mobile.bnkcl.data.api.MGApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class MGRepo(private val mgApi: MGApi) {

    fun getMGData(): Flow<MGData> = flow {
        delay(1000)
        try {
            val request = object: RemoteDataSource<MGData>(){
                override suspend fun createCall(): Response<MGData> {
                    return mgApi.getMGData("AOS")
                }
            }
            request.networkRequest()
            emit(request.asLiveData().value?.data!!)
        } catch (e: Exception) {
        }
    }

}