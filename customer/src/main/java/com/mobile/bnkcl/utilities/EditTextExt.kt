package com.mobile.bnkcl.utilities

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class NumberTextWatcherForThousand internal constructor(var editText: EditText) : TextWatcher {
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    override fun afterTextChanged(s: Editable) {
        val value = s.toString()
        try {
            editText.removeTextChangedListener(this)
            val fixedValue: String = FormatUtil.getDecimalFormattedString(value, false).toString()
            val preSelection = editText.selectionEnd
            s.replace(0, value.length, fixedValue)
            val selection = preSelection + fixedValue.length - value.length
//            DevLog.devLog(">>>>>>>", ">>>>>>>>>>> :: " + s.length)
            editText.setSelection(Math.max(s.length, 0))
//            try {
//                if (editText === mBinding.edLoanAmount) { //_Auto generate of amount when user input amount in foreign currency and exchange rate
//                    viewModel.setLOAN_AMOUNT(s.toString().replace(",".toRegex(), ""))
//                } else if (editText === mBinding.edMonthlyIncome) {
//                    viewModel.setMONTHLY_INCOME(s.toString().replace(",".toRegex(), ""))
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
            editText.addTextChangedListener(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
