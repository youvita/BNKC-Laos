package com.mobile.bnkcl.data.api.cscenter

import com.mobile.bnkcl.data.response.cscenter.ClaimDetailRes
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ClaimDetailApi {
    @GET("api/ca/claims/{claim_id}")
    suspend fun getClaimDetailData(@Path("claim_id") claim_id: Int?): Response<ClaimDetailRes>
}