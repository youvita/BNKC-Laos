package com.mobile.bnkcl.data.request.cscenter

import com.google.gson.annotations.SerializedName

 data class ClaimDataRequest (

    @SerializedName("page_number")
    var page_number: Int = 0,

     @SerializedName("page_size")
     var page_size: Int = 10,

     @SerializedName("sort_by")
     var sort_by: String? = null


)