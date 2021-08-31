package com.mobile.bnkcl.data.response.lease

import com.google.gson.annotations.SerializedName

data class LeaseInfoData (

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
    val repaymentDate: String? = null,

    @SerializedName("monthly_repayment")
    val monthlyRepayment: String? = null

)