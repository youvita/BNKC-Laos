package com.mobile.bnkcl.com.binding

import android.view.View
import androidx.databinding.BindingAdapter
import com.mobile.bnkcl.com.view.pincode.PinView

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
                    title?.text = "Reset PIN"
                    mResetPin!!.visibility = View.GONE
                    mPinMessage!!.text = "PIN Registration"
                }

            }
            2 -> { //sign up
                title?.text = "Set Up New PIN"
                mPinMessage!!.visibility = View.VISIBLE
                mPinMessage!!.text = "PIN Registration"
            }
            3 -> { //forget
                title?.text = "Forget PIN"
            }
            4 -> { //Reset pin
                title?.text = "Reset PIN"
                mPinMessage!!.visibility = View.VISIBLE
                mPinMessage!!.text = "PIN Registration"
            }
        }
    }
}