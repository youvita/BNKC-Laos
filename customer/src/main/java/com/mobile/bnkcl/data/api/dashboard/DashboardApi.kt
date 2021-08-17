package com.mobile.bnkcl.data.api.dashboard

import com.mobile.bnkcl.data.response.dashboard.DashboardResponse
import com.mobile.bnkcl.data.response.user.ProfileData
import com.mobile.bnkcl.data.response.user.SettingData
import retrofit2.Response
import retrofit2.http.*

interface DashboardApi {

    @GET("api/ca/my-leases/dashboard")
    suspend fun getDashboard(): Response<DashboardResponse>

}