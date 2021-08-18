package com.mobile.bnkcl.data.api.lease

import com.mobile.bnkcl.data.response.lease.LeaseInfoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface LeaseApi {

    @GET("api/ca/my-leases/{contract_no}")
    suspend fun getLeaseInfo(@Path("contract_no") contract_no: String): Response<LeaseInfoResponse>

}