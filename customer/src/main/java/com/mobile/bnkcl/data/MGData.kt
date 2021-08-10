package com.mobile.bnkcl.data

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

data class MGData(

    @SerializedName("_master_id")
    val masterId: String? = null,

    @SerializedName("c_alert_mode")
    val cAlertMode: String? = null,

    @SerializedName("c_alert_msg")
    val cAlertMSG: String? = null,

    @SerializedName("c_appstore_url")
    val cAppStoreUrl: String? = null,

    @SerializedName("c_program_ver")
    val cProgramVer: String? = null,

    @SerializedName("c_service_mode")
    val cServiceMode: String? = null,

    @SerializedName("c_service_msg")
    val cServiceMSG: String? = null,

    @SerializedName("c_start_url")
    val cStartUrl: String? = null

)
