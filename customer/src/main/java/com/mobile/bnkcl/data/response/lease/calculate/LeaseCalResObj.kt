package com.mobile.bnkcl.data.response.lease.calculate

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LeaseCalResObj(

    @SerializedName("principal")
    var principal: String? = null,

    @SerializedName("seq")
    var seq: Int? = 0,

    @SerializedName("repayment")
    var repayment: String? = null,

    @SerializedName("balance")
    var balance: String? = null,

    @SerializedName("interest")
    var interest: String? = null

) : Serializable
