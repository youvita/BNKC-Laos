package com.mobile.bnkcl.data.api.office

import com.bnkc.library.util.Constants
import com.mobile.bnkcl.data.findoffice.BranchResData
import com.mobile.bnkcl.data.request.findoffice.AreaRequest
import com.mobile.bnkcl.data.request.findoffice.BranchRequest
import com.mobile.bnkcl.data.response.area.AreaResponse
import com.mobile.bnkcl.data.response.area.BranchResponse
import retrofit2.Response
import retrofit2.http.*

interface FindOfficeAPI {

    @GET("/api/ca/areas")
    suspend fun getAreas(
        @Query("category") category: String
    ) : Response<AreaResponse>

    @GET("/api/ca/branches")
    suspend fun getBranches(
        @Query("area_id") area_id: String,
        @Query("page_number") page_number: Int,
        @Query("page_size") page_size: Int,
        @Query("sort_by") sort_by: String? = null
    ) : Response<BranchResponse>

    @GET("/api/ca/branches/{branch_id}") // /api/ca/branches/13
    suspend fun getBranchInfo(@Path("branch_id") branch_id: Long) : Response<BranchResData>

}