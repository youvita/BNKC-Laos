package com.mobile.bnkcl.com.alert

import android.content.Context
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.mobile.bnkcl.R
import java.util.*

class DlgLanguage : View.OnClickListener{

    private var dialogWindow: DlgWindow? = null
    private var mContext: Context? = null

    fun dlgLanguage(
        context: Context,
        lang: String,
        cancel_able: Boolean
    ): DlgWindow? {
        mContext = context.applicationContext
        val mLayout = View.inflate(mContext, R.layout.alert_language, null)
        dialogWindow = DlgWindow(context, mLayout)
        dialogWindow!!.setCancelable(cancel_able)
        dialogWindow!!.setCanceledOnTouchOutside(cancel_able)
        dialogWindow!!.show()

        setSelection(mLayout, lang)
        val tvTitle = mLayout.findViewById<TextView>(R.id.alertTitle)
        val radioGroup = mLayout.findViewById<RadioGroup>(R.id.rg_langauge)
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val rb = radioGroup.findViewById<View>(checkedId) as RadioButton
            val index = radioGroup.indexOfChild(rb)
            if (index == 0) {
                delay()
            } else {
                delay()
            }
        }
        return dialogWindow
    }

    private fun delay(){
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                dialogWindow!!.dismiss()
                timer.cancel() //this will cancel the timer of the system
            }
        }, 300)
    }

    private fun setSelection(view: View, locale: String) {
        val rbLaos = view.findViewById<RadioButton>(R.id.rb_la)
        val rbEnglish = view.findViewById<RadioButton>(R.id.rb_en)
        val tvTitle = view.findViewById<TextView>(R.id.alertTitle)
        if (locale.equals("la", ignoreCase = true)) {
            tvTitle.text = "ພາສາລາວ"
            rbLaos.isChecked = true
        } else {
            tvTitle.text = "English"
            rbEnglish.isChecked = true
        }
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }
}