package com.mobile.bnkcl.data.response.area

import com.google.gson.annotations.SerializedName

class AreaObjResponse {
    @SerializedName("id")
    private val id: Long = 0

    @SerializedName("name")
    private val name: String? = null

    @SerializedName ("erp_code")
    private val erp_code : String? = null

    @SerializedName("iso_code")
    private val iso_code: String? = null

    @SerializedName("alias1")
    private val alias1: String? = null

    @SerializedName("branch_count")
    private val branch_count: String? = null

    @SerializedName("alias2")
    private val alias2: String? = null
}