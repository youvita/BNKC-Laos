package com.mobile.bnkcl.data.response.user

import com.google.gson.annotations.SerializedName
import com.mobile.bnkcl.data.response.user.profile.Address

data class ProfileData(

        @SerializedName("family_name")
        val family_name: String? = null,

        @SerializedName("given_name")
        val given_name: String? = null,

        @SerializedName("gender")
        val gender: String? = null,

        @SerializedName("date_of_birth")
        val date_of_birth: String? = null,

        @SerializedName("address")
        val address: Address? = null,

        @SerializedName("occupation")
        val occupation: String? = null,

        @SerializedName("push_alarm_enabled")
        val push_alarm_enabled: String? = null,

        @SerializedName("my_loans")
        val my_loans: List<String>? = null
)
