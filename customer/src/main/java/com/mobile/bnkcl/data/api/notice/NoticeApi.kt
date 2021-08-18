package com.mobile.bnkcl.data.api.notice

import com.mobile.bnkcl.data.response.notice.NoticeDataResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NoticeApi {

    @GET("api/ca/notices")
    suspend fun getNotice(@Query("title") title: String?,
                          @Query("page_number") page_number: Int?,
                          @Query("page_size") page_size: Int?,
                          @Query("sort_by") sort_by: String?) : Response<NoticeDataResponse>
}