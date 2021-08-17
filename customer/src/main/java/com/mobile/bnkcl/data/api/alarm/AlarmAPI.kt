package com.mobile.bnkcl.data.api.alarm

import com.mobile.bnkcl.data.response.alarm.AlarmResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AlarmAPI {
    @GET("api/ca/alarms")
    suspend fun getAlarmList(
        @Query("page_number") page_number: Int,
        @Query("page_size") page_size: Int,
        @Query("sort_by") sort_by: String? = null
    ): Response<AlarmResponse>
}