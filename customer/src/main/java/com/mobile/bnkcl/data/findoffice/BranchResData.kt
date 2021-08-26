package com.mobile.bnkcl.data.findoffice

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class BranchResData : Serializable{

    @SerializedName("id")
    var id: Long = 0

    @SerializedName("name")
    var name: String? = null

    @SerializedName("area_name")
    var area_name: String? = null

    @SerializedName("tel")
    var tel: String? = null

    @SerializedName("email")
    var email: String? = null

    @SerializedName("address")
    var address: String? = null

    @SerializedName("location")
    var location: Location? = null

}