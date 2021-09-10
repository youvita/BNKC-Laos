package com.mobile.bnkcl.data.response.user

import com.google.gson.annotations.SerializedName

data class SettingData(

        @SerializedName("push_alarm_enabled")
        var push_alarm_enabled: Boolean? = null

)