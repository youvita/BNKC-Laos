package com.mobile.bnkcl.data.response.dashboard

import com.google.gson.annotations.SerializedName

data class LeaseApplicationResponse (

    @SerializedName("lease_applications")
    val leaseApplications: ArrayList<LeaseApplicationData>? = null

)