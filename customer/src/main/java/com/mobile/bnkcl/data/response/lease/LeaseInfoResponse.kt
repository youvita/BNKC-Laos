package com.mobile.bnkcl.data.response.lease

import com.google.gson.annotations.SerializedName

data class LeaseInfoResponse(

    @SerializedName("contract_no")
    val contractNo: String? = null,

    @SerializedName("product_type")
    val productType: String? = null,

    @SerializedName("lease_amount")
    val leaseAmount: String? = null,

    @SerializedName("lease_interest_rate")
    val leaseInterestRate: String? = null,

    @SerializedName("lease_term")
    val leaseTerm: String? = null,

    @SerializedName("disbursement_date")
    val disbursementDate: String? = null,

    @SerializedName("maturity_date")
    val maturityDate: String? = null,

    @SerializedName("repayment_day")
    val repaymentDay: String? = null,

    @SerializedName("monthly_repayment")
    val monthlyRepayment: String? = null,

    @SerializedName("coming_repayment_amount")
    val comingRepaymentAmount: String? = null,

    @SerializedName("coming_repayment_date")
    val comingRepaymentDate: String? = null,

    @SerializedName("repayment_session")
    val repaymentSession: String? = null,

    @SerializedName("principal")
    val principal: String? = null,

    @SerializedName("interest")
    val interest: String? = null,

    @SerializedName("overdue_principal")
    val overduePrincipal: String? = null,

    @SerializedName("overdue_interest")
    val overdueInterest: String? = null,

    @SerializedName("overdue_penalty")
    val overduePenalty: String? = null,

    @SerializedName("overdue_paralty_days")
    val overdueParaltyDays: String? = null,

    @SerializedName("total")
    val total: String? = null,

)