package com.mobile.bnkcl.data.request.area

import com.google.gson.annotations.SerializedName

class AddressData (

    @SerializedName("category")
    var category: String? = null,

    @SerializedName("parent_id")
    var parent_id: String? = null,
)