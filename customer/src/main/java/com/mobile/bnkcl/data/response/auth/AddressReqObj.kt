package com.mobile.bnkcl.data.response.auth

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class AddressReqObj : Serializable {

    @SerializedName("state")
    var state: AreaObj? = null

    @SerializedName("district")
    var district : AreaObj? = null

    @SerializedName("commune")
    var commune: AreaObj? = null

    @SerializedName("village")
    var village: AreaObj? = null

    @SerializedName("more_info")
    var more_info: String? = null

}