package com.mobile.bnkcl.data.response.user.profile

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ProfileAddress(

        @SerializedName("state")
        val state: Area? = null,

        @SerializedName("district")
        val district: Area? = null,

        @SerializedName("commune")
        val commune: Area? = null,

        @SerializedName("village")
        val village: Area? = null,

        @SerializedName("more_info")
        val more_info: String? = null,
) : Serializable
