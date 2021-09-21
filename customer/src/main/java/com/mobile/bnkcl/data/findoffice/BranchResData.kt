package com.mobile.bnkcl.data.findoffice

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class BranchResData : Serializable{

    @SerializedName("branch_id")
    var branch_id: Long? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("area_name")
    var area_name: String? = null

    @SerializedName("tel")
    var tel: String? = null

    @SerializedName("email")
    var email: String? = null

    @SerializedName("fb_page_name")
    var fb_page_name: String? = null

    @SerializedName("fb_page_url")
    var fb_page_url: String? = null

    @SerializedName("address")
    var address: String? = null

    @SerializedName("location")
    var location: Location? = null

}