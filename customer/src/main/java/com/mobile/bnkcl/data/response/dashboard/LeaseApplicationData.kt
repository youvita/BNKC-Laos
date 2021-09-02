package com.mobile.bnkcl.data.response.dashboard

import com.google.gson.annotations.SerializedName

data class LeaseApplicationData(

    @SerializedName("lease_application_id")
    val leaseApplicationId: String? = null,
    @SerializedName("product_type")
    val productType: String? = null,
    @SerializedName("etc_status")
    val etcStatus: String? = null,
    @SerializedName("name_of_brand")
    val nameOfBrand: String? = null,
    @SerializedName("name_of_type")
    val nameOfType: String? = null,
    @SerializedName("name_of_model")
    val nameOfModel: String? = null,
    @SerializedName("etc_name_of_type")
    val etcNameOfType: String? = null,
    @SerializedName("etc_name_of_model")
    val etcNameOfModel: String? = null,
    @SerializedName("etc_name_of_branch")
    val etcNameOfBranch: String? = null,
    @SerializedName("product_price")
    val productPrice: String? = null,
    @SerializedName("request_amount")
    val requestAmount: String? = null,
    @SerializedName("advance_payment")
    val advancePayment: String? = null,
    @SerializedName("repayment_term")
    val repaymentTerm: String? = null,
    @SerializedName("rejection_reason")
    val rejectionReason: String? = null,
    @SerializedName("progress_status")
    val progressStatus: String? = null,
    @SerializedName("dealer_name")
    val dealerName: String? = null,
    @SerializedName("dealer_phone_number")
    val dealerPhoneNumber: String? = null

)