package com.mobile.bnkcl.data.response.lease.apply

import com.google.gson.annotations.SerializedName

data class ApplyLeaseResponse(
    @SerializedName("lease_application_id")
    var lease_application_id : String? = null
)
