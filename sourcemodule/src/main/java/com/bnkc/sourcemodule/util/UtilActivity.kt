package com.bnkc.sourcemodule.util

class UtilActivity {

    companion object{
        private var isCreated = false

//    var loanResultActivity: LoanResultActivity? = null
//    var applyLoanActivity: ApplyLoanActivity? = null
//    var microLoanActivity: MicroLoanActivity? = null

        fun isCreated(): Boolean {
            return isCreated
        }

        fun isCreated(isCreated: Boolean) {
            this.isCreated = isCreated
        }
    }

}