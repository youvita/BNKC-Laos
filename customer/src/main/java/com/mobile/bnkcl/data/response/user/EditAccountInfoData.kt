package com.mobile.bnkcl.data.response.user

import com.google.gson.annotations.SerializedName

data class EditAccountInfoData(

    @SerializedName("job_type")
    var job_type: String? = null,

    @SerializedName("bank_name")
    var bank_name: String? = null,

    @SerializedName("account_number")
    var account_number: String? = null

)