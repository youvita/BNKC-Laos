package com.mobile.bnkcl.data.repository.findoffice

import android.content.Context
import com.bnkc.library.data.network.RemoteDataSource
import com.bnkc.library.data.type.Resource
import com.bnkc.sourcemodule.app.Constants
import com.bnkc.sourcemodule.app.RetrofitBuilder
import com.mobile.bnkcl.data.api.office.FindOfficeAPI
import com.mobile.bnkcl.data.findoffice.BranchResData
import com.mobile.bnkcl.data.request.findoffice.AreaRequest
import com.mobile.bnkcl.data.request.findoffice.BranchRequest
import com.mobile.bnkcl.data.response.area.AreaResponse
import com.mobile.bnkcl.data.response.area.BranchResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import retrofit2.Response
import javax.inject.Inject

class FindOfficeRepo @Inject constructor(context: Context, okHttpClient: OkHttpClient) {

    private val findAPI: FindOfficeAPI by lazy { RetrofitBuilder(context, okHttpClient).getRetrofit().create(
        FindOfficeAPI::class.java) }

    fun getAreas(areaRequest: AreaRequest): Flow<Resource<AreaResponse>> = flow {
        try {
            delay(1000)
            val request = object : RemoteDataSource<AreaResponse>() {
                override suspend fun createCall(): Response<AreaResponse> {
                    return findAPI.getAreas(areaRequest.category!!)
                }
            }
            request.networkRequest()
            emit(request.asLiveData().value!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getBranches(branchRequest: BranchRequest): Flow<Resource<BranchResponse>> = flow {
        try {
            delay(1000)
            val request = object : RemoteDataSource<BranchResponse>() {
                override suspend fun createCall(): Response<BranchResponse> {
                    return findAPI.getBranches(branchRequest.area_id!!, branchRequest.page_number!!, branchRequest.page_size!!, branchRequest.sort_by)
                }
            }
            request.networkRequest()
            emit(request.asLiveData().value!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getOffice(branchId : Long): Flow<Resource<BranchResData>> = flow {
        try {
            delay(1000)
            val request = object : RemoteDataSource<BranchResData>() {
                override suspend fun createCall(): Response<BranchResData> {
                    return findAPI.getBranchInfo(branchId)
                }
            }
            request.networkRequest()
            emit(request.asLiveData().value!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}