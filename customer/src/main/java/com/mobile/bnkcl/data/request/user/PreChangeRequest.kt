package com.mobile.bnkcl.data.request.user

import com.google.gson.annotations.SerializedName

class PreChangeRequest(

    @SerializedName("password")
    var password : String? = null

)