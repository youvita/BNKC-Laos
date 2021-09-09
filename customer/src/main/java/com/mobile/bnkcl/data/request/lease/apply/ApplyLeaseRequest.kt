package com.mobile.bnkcl.data.request.lease.apply

import com.google.gson.annotations.SerializedName

class ApplyLeaseRequest {

    @SerializedName("product_type")
    var product_type : String? = null

    @SerializedName("etc_status")
    var etc_status : Boolean? = false

    @SerializedName("etc_name_of_brand")
    var etc_name_of_brand : String? = null

    @SerializedName("etc_name_of_type")
    var etc_name_of_type : String? = null

    @SerializedName("etc_name_of_model")
    var etc_name_of_model : String? = null

    @SerializedName("name_of_brand")
    var name_of_brand : String? = null

    @SerializedName("name_of_type")
    var name_of_type : String? = null

    @SerializedName("name_of_model")
    var name_of_model : String? = null

    @SerializedName("product_price")
    var product_price : String? = null

    @SerializedName("request_amount")
    var request_amount : String? = null

    @SerializedName("repayment_term")
    var repayment_term : String? = null

}