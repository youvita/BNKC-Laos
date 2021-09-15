package com.mobile.bnkcl.com.binding

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.databinding.BindingAdapter
import com.mobile.bnkcl.com.view.BnkEditText
import com.mobile.bnkcl.com.view.pincode.PinView
import com.mobile.bnkcl.ui.lease.apply.ApplyLeaseViewModel
import com.mobile.bnkcl.utilities.FormatUtil

object PinBindingAdapter {

    @JvmStatic
    @BindingAdapter("displayPinUI")
    fun PinView.displayPinUI(type : Int){
        when(type){
            1 -> { //login
                title?.text = "Log In"
                mPinMessage!!.visibility = View.VISIBLE
                mPinMessage!!.text = "Forget PIN?"

                mResetPin!!.setOnClickListener {
                    setOnActionListener("reset_pin")
                }

            }
            2 -> { //sign up
                title?.text = "Set Up New PIN"
                mPinMessage!!.visibility = View.VISIBLE
                mResetPin!!.visibility = View.GONE
                mPinMessage!!.text = "PIN Registration"
            }
            3 -> { //forget
                title?.text = "Forget PIN"
                mPinMessage!!.visibility = View.VISIBLE
                mPinMessage!!.text = "New PIN"
            }
            4 -> { //Reset pin
                title?.text = "Reset PIN"
                mPinMessage!!.visibility = View.VISIBLE
                mPinMessage!!.text = "PIN Registration"
            }
        }
    }

    @BindingAdapter("app:addTextChangeListener")
    fun addTextChangeListener(editText: BnkEditText, viewModel: ApplyLeaseViewModel) {

        editText.addTextChangedListener(object : TextWatcher {
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
                    editText.addTextChangedListener(this)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        })

    }

}