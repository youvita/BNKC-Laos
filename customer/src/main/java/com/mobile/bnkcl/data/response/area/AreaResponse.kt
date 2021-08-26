package com.mobile.bnkcl.data.response.area

import com.google.gson.annotations.SerializedName
import com.mobile.bnkcl.data.response.office.AreaDataResponse

class AreaResponse {
    @SerializedName("areas")
    val areas: List<AreaDataResponse>? = null

}