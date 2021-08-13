package com.mobile.bnkcl.data.response.common

import com.google.gson.annotations.SerializedName

data class MGDataResponse(

    @SerializedName("_master_id")
    val masterId: String? = null,

    @SerializedName("c_act_msg")
    val c_act_msg: String? = null,

    @SerializedName("c_act_ser_msg")
    val c_act_ser_msg: String? = null,

    @SerializedName("c_act_update")
    val c_act_update: Boolean? = null,

    @SerializedName("c_act_update_msg")
    val c_act_update_msg: String? = null,

    @SerializedName("c_act_yn")
    val c_act_yn: Boolean? = null,

    @SerializedName("c_appstore_url")
    val c_appstore_url: String? = null,

    @SerializedName("c_available_service")
    val c_available_service: Boolean? = null,

    @SerializedName("c_available_service_msg")
    val c_available_service_msg: Boolean? = null,

    @SerializedName("c_program_ver")
    val c_program_ver: String? = null,

    @SerializedName("c_start_url")
    val c_start_url: String? = null,

)
