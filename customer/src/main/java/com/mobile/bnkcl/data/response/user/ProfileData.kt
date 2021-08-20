package com.mobile.bnkcl.data.response.user

import com.google.gson.annotations.SerializedName
import com.mobile.bnkcl.data.response.user.profile.Address
import com.mobile.bnkcl.data.response.user.profile.ProfileAddress

data class ProfileData(

        @SerializedName("name")
        var name: String? = null,

        @SerializedName("date_of_birth")
        var date_of_birth: String? = null,

        @SerializedName("gender")
        var gender: String? = null,

        @SerializedName("phone_number")
        var phone_number: String? = null,

        @SerializedName("identification_number")
        var identification_number: String? = null,

        @SerializedName("address")
        var address: ProfileAddress? = null,

        @SerializedName("etc_status")
        var occupation: Boolean? = null,

        @SerializedName("etc_detailed_address")
        var etc_detailed_address: String? = null,

        @SerializedName("job_type")
        var job_type: String? = null,

        @SerializedName("bank_name")
        var bank_name: String? = null,

        @SerializedName("account_number")
        var account_number: String? = null,

        @SerializedName("push_alarm_enabled")
        var push_alarm_enabled: String? = null,

        @SerializedName("my_lease")
        var my_lease: List<String>? = null
)
