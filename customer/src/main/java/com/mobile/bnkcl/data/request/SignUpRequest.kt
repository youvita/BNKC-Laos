package com.mobile.bnkcl.data.request

import com.google.gson.annotations.SerializedName

data class SignUpRequest(
        @SerializedName ("session_id")
        var session_id: String? = null,

        @SerializedName("date_of_birth")
        var date_of_birth: String? = null,

        @SerializedName("family_name")
        var family_name: String? = null,

        @SerializedName("sex")
        var sex: String? = null,

        @SerializedName("given_name")
        var given_name: String? = null,

        @SerializedName("phone_number")
        var phone_number: String? = null,

        @SerializedName("identification_number")
        var identification_number: String? = null,

        @SerializedName("job_type")
        var job_type: String? = null,

        @SerializedName("recommender")
        var recommender: String? = null,

        @SerializedName("password")
        var password: String? = null
    
        )