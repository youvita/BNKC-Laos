package com.mobile.bnkcl.data.api.lease.total_schedule

import com.mobile.bnkcl.data.response.lease.total_lease_schedules.TotalLeaseScheduleResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TotalLeaseScheduleApi {

    @GET("api/ca/my-leases/{contract_no}/schedules")
    suspend fun getTotalLeaseSchedule(@Path("contract_no") contract_no: String,
                                      @Query("payment_date.dir") sort_by: String? ): Response<TotalLeaseScheduleResponse>

}