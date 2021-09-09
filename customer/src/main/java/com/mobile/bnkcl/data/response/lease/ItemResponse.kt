package com.mobile.bnkcl.data.response.lease

import com.google.gson.annotations.SerializedName

data class ItemResponse(
    @SerializedName("codes")
    val codes: ArrayList<ItemResponseObject>? = null
)
