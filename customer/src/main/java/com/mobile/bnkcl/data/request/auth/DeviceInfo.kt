package com.mobile.bnkcl.data.request.auth

import com.google.gson.annotations.SerializedName

data class DeviceInfo(

    @SerializedName("push_id")
    var push_id: String? = null,

    @SerializedName("platform_os")
    var platform_os: String? = null,

    @SerializedName("model_name")
    var model_name: String? = null,

    @SerializedName("os_version")
    var os_version: String? = null

)