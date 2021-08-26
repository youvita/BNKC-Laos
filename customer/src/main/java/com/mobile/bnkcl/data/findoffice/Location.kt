package com.mobile.bnkcl.data.findoffice

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Location : Serializable {
    @SerializedName("lat")
    var lat = 0.0

    @SerializedName("lon")
    var lon = 0.0

    @SerializedName("zoom_level")
    var zoom_level = 0

}