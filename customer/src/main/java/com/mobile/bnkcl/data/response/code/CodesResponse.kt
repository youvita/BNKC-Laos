package com.mobile.bnkcl.data.response.code

import com.google.gson.annotations.SerializedName

data class CodesResponse(

    @SerializedName("codes")
    val codes: ArrayList<CodesData>? = null

)