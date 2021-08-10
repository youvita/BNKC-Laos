package com.mobile.bnkcl.data.findoffice

import com.google.gson.annotations.SerializedName

class BranchResData {

    @SerializedName("id")
    val id: Long = 0

    @SerializedName("name")
    val name: String? = null

    @SerializedName("tel")
    val tel: String? = null

    @SerializedName("email")
    val email: String? = null

    @SerializedName("address")
    val address: String? = null

    @SerializedName("location")
    val location: Location? = null

}