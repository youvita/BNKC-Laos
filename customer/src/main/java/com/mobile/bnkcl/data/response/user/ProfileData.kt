package com.mobile.bnkcl.data.response.user

import com.google.gson.annotations.SerializedName
import com.mobile.bnkcl.data.response.user.profile.ProfileAddress
import java.io.Serializable
import java.lang.Exception

data class ProfileData(

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("date_of_birth")
    var dateOfBirth: String? = null,

    @SerializedName("gender")
    var gender: String? = null,

    @SerializedName("phone_number")
    var phoneNumber: String? = null,

    @SerializedName("identification_number")
    var identificationNumber: String? = null,

    @SerializedName("address")
    var address: ProfileAddress? = null,

    @SerializedName("etc_status")
    var edtStatus: Boolean? = null,

    @SerializedName("etc_detailed_address")
    var etcDetailedAddress: String? = null,

    @SerializedName("job_type")
    var jobType: String? = null,

    @SerializedName("bank_name")
    var bankName: String? = null,

    @SerializedName("account_number")
    var accountNumber: String? = null,

    @SerializedName("push_alarm_enabled")
    var pushAlarmEnabled: Boolean? = null,

    @SerializedName("my_leases")
    var myLease: List<MyLeaseData>? = null

) : Serializable {
    fun getLeaseText(): String? {
        var myLeaseText = ""
        return try {
            if (myLease!!.isNotEmpty()) {
                for (lease in myLease!!) {
                    myLeaseText = myLeaseText.plus(lease.contractNo).plus("\n")
                }
            }
            myLeaseText.trim { it <= ' ' }
        } catch (e: Exception) {
            e.printStackTrace()
            myLeaseText
        }
    }
}