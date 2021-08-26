package com.mobile.bnkcl.data.response.area

import com.google.gson.annotations.SerializedName
import com.mobile.bnkcl.data.findoffice.BranchResData

class BranchResponse {

    @SerializedName("branches")
    val branches: List<BranchResData>? = null

}