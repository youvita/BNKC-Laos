package com.mobile.bnkcl.data.response.user.profile

import com.google.gson.annotations.SerializedName

data class Address(

        @SerializedName("state")
        val state: State? = null,

        @SerializedName("district")
        val district: District? = null,

        @SerializedName("commune")
        val commune: Commune? = null,

        @SerializedName("village")
        val village: Village? = null,

        @SerializedName("more_info")
        val more_info: String? = null,
)
