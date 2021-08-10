package com.mobile.bnkcl.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class MyLoan{
    @SerializedName("contract_no")
    var contractNo: String? = null

    @SerializedName("loan_amount_type")
    var loanAmountType: String? = null

    @SerializedName("loan_balance")
    var loanBalance: String? = null

    @SerializedName("repayment_date")
    var repaymentDate: String? = null

    @SerializedName("repayment_amount")
    var repaymentAmount: String? = null

    @SerializedName("overdue_amount")
    var overdueAmount: String? = null

    override fun toString(): String {
        return "MyLoan{" +
                "contractNo='" + contractNo + '\'' +
                ", loanAmountType='" + loanAmountType + '\'' +
                ", loanBalance='" + loanBalance + '\'' +
                ", repaymentDate='" + repaymentDate + '\'' +
                ", repaymentAmount='" + repaymentAmount + '\'' +
                ", overdueAmount='" + overdueAmount + '\'' +
                '}'
    }
}