package com.mobile.bnkcl.data.request.auth

import com.google.gson.annotations.SerializedName
import com.mobile.bnkcl.data.request.area.AddressData
import com.mobile.bnkcl.data.response.auth.AddressReqObj
import java.io.Serializable

data class SignUpRequest(
        @SerializedName ("phone_number")
        var phone_number: String? = null,

        @SerializedName ("password")
        var password: String? = null,

        @SerializedName ("session_id")
        var session_id: String? = null,

        @SerializedName("name")
        var name: String? = null,

        @SerializedName("date_of_birth")
        var date_of_birth: String? = null,

        @SerializedName("gender")
        var gender: String? = null,

        @SerializedName("identification_number")
        var identification_number: String? = null,

        @SerializedName("etc_status")
        var etc_status: Boolean? = null,

        @SerializedName("address")
        var address: AddressReqObj? = null,

        @SerializedName("etc_detailed_address")
        var etc_detailed_address: String? = null,

        @SerializedName("bank_name")
        var bank_name: String? = null,

        @SerializedName("account_number")
        var account_number: String? = null,

        @SerializedName("job_type")
        var job_type: String? = null,

        @SerializedName("recommender")
        var recommender: String? = null,

        ) : Serializable