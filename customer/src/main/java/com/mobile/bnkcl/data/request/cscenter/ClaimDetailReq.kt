package com.mobile.bnkcl.data.request.cscenter

import com.google.gson.annotations.SerializedName

class ClaimDetailReq (
        @SerializedName("claim_id")
        var claim_id: Int? = null
)