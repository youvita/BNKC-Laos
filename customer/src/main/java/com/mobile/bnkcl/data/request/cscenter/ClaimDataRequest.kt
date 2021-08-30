package com.mobile.bnkcl.data.request.cscenter

import com.google.gson.annotations.SerializedName

 class ClaimDataRequest (

    @SerializedName("page_number")
    var page_number: Int? = null,

     @SerializedName("page_size")
     var page_size: Int? = null,

     @SerializedName("sort_by")
     var sort_by: String? = null


)