package com.mobile.bnkcl.data.request.lease

import com.google.gson.annotations.SerializedName

data class LeaseInfoRequest(

    @SerializedName("contract_no")
    var contract_no: String? = null

)