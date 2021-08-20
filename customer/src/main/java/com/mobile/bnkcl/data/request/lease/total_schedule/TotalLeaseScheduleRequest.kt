package com.mobile.bnkcl.data.request.lease.total_schedule

import com.google.gson.annotations.SerializedName

data class TotalLeaseScheduleRequest(

    @SerializedName("contract_no")
    var contract_no: String? = null,

    @SerializedName("payment_date.dir")
    var payment_date_dir: String? = null

)