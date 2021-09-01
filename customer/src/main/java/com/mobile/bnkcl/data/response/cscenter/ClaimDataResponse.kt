package com.mobile.bnkcl.data.response.cscenter

import androidx.paging.PagedList
import com.bnkc.library.data.type.Resource
import com.google.gson.annotations.SerializedName

class ClaimDataResponse {

    @SerializedName("claims")
    var claims: List<ClaimItems>? = null

    @SerializedName("last")
    var last: Boolean? = null

    @SerializedName("first")
    var first: Boolean? = null


    @SerializedName("size")
    var size: Int? = null

    @SerializedName("number")
    var number: Int? = null

    @SerializedName("total_pages")
    var total_pages: Int? = null


    @SerializedName("total_elements")
    var total_elements: Int? = null


}