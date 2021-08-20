package com.mobile.bnkcl.data.response.lease

import com.google.gson.annotations.SerializedName

data class ComingRepaymentInfoData (

    @SerializedName("coming_repayment_date")
    val comingRepaymentDate: String? = null,

    @SerializedName("repayment_session")
    val repaymentSession: String? = null,

    @SerializedName("principal")
    val principal: String? = null,

    @SerializedName("interest")
    val interest: String? = null,

    @SerializedName("overdue_principle")
    val overduePrinciple: String? = null,

    @SerializedName("overdue_interest")
    val overdueInterest: String? = null,

    @SerializedName("overdue_penalty")
    val overduePenalty: String? = null,

    @SerializedName("overdue_penalty_days")
    val overduePenaltyDays: Int? = null,

    @SerializedName("total")
    val total: String? = null

)