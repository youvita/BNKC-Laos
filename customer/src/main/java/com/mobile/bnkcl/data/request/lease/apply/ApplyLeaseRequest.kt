package com.mobile.bnkcl.data.request.lease.apply

import com.google.gson.annotations.SerializedName

data class ApplyLeaseRequest (

    @SerializedName("product_type")
    var product_type : String? = null,

    @SerializedName("brand_name")
    var brand_name : String? = null,

    @SerializedName("model_name")
    var model_name : String? = null,

    @SerializedName("product_price")
    var product_price : String? = null,

    @SerializedName("request_amount")
    var request_amount : String? = null,

    @SerializedName("advance_payment")
    var advance_payment : String? = null,

    @SerializedName("repayment_term")
    var repayment_term : String? = null,

        )