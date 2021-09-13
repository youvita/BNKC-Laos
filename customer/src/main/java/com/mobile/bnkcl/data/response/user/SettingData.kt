package com.mobile.bnkcl.data.response.user

import com.google.gson.annotations.SerializedName
import com.mobile.bnkcl.data.request.auth.DeviceInfo

data class SettingData(

    @SerializedName("push_alarm_enabled")
    var push_alarm_enabled: Boolean? = null,

    @SerializedName("device_info")
    var device_info: DeviceInfo? = null
)