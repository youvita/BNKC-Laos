package com.mobile.bnkcl.data.response.user

import com.google.gson.annotations.SerializedName

data class EditAccountInfoData(

    @SerializedName("job_type")
    var jobType: String? = null,

    @SerializedName("bank_name")
    var bankName: String? = null,

    @SerializedName("account_number")
    var accountNumber: String? = null

)