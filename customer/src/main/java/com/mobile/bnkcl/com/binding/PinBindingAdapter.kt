package com.mobile.bnkcl.com.binding

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.mobile.bnkcl.R
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
                title?.text = context.getString(R.string.nav_login)
                mPinMessage!!.visibility = View.VISIBLE
                mPinMessage!!.text = context.getString(R.string.forget_pin_ask)
                mResetPin!!.text = context.getString(R.string.comm_reset_now)

                mResetPin!!.setOnClickListener {
                    setOnActionListener("reset_pin")
                }
            }
            2 -> { //sign up
                mBackButton!!.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_close_white))
                title?.text = context.getString(R.string.set_up_pin)
                mPinMessage!!.visibility = View.VISIBLE
                mResetPin!!.visibility = View.GONE
                mPinMessage!!.text = context.getString(R.string.pin_registration)
            }
            3 -> { //forget
                title?.text = context.getString(R.string.forget_pin)
                mPinMessage!!.visibility = View.VISIBLE
                mPinMessage!!.text = context.getString(R.string.new_pin)
            }
            4 -> { //Reset pin
                mBackButton!!.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_close_white))
                title?.text = context.getString(R.string.setting_reset_pin)
                mPinMessage!!.visibility = View.VISIBLE
                mPinMessage!!.text = context.getString(R.string.pin_05)
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

                    editText.setSelection(Math.max(s.length, 0))
                    editText.addTextChangedListener(this)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        })

    }

}