package com.mobile.bnkcl.data.api.faq

import com.mobile.bnkcl.data.response.faq.FaqRes
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FaqsApi {
    @GET("api/ca/faqs")
    suspend fun getFaqData(
            @Query("page_number") page_number: Int?,
            @Query("page_size") page_size: Int?,
            @Query("sort_by") sort_by: String?) : Response<FaqRes>
}