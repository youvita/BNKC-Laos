package com.mobile.bnkcl.data.response.user

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MyLeaseData(

    @SerializedName("contract_no")
    var contractNo: String? = null

) : Serializable