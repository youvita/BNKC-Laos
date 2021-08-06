package com.mobile.bnkcl.utilities

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.mobile.bnkcl.R
import java.util.*

class ValidateButton : LinearLayout {

    var tvCheckLabel: TextView? = null
    var llCheckLabel: LinearLayout? = null

    var status = false
    private var isTextCaps: kotlin.Boolean = false

    constructor(context: Context?) : super(context) {

        initView(context!!, null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {

        initView(context!!, attrs!!)
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {

        initView(context!!, attrs!!)
    }

    fun initView(context: Context, attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.ValidateButton)
        val text = a.getString(R.styleable.ValidateButton_buttonText)
        val active = a.getBoolean(R.styleable.ValidateButton_is_active, false)
        isTextCaps = a.getBoolean(R.styleable.ValidateButton_textAllCaps, true)
        val background = a.getResourceId(R.styleable.ValidateButton_buttonBackground, 0)
        val textColor = a.getColor(R.styleable.ValidateButton_buttonTextColor, 0)
        val inflate = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
        val mView: View = inflate.inflate(R.layout.comm_validate_button, this, true)
        llCheckLabel = mView.findViewById(R.id.ll_check_button)
        tvCheckLabel = mView.findViewById(R.id.tv_check_button)
//        tvCheckLabel.setTextAppearance(context, R.style.font_14_FFFFFF_normal)
        setLabelButton(text!!)
        if (active) {
            setActive(true)
        } else {
            if (background != 0 || textColor != 0) {
                setNormalButton(background, textColor)
            } else {
                setActive(false)
            }
        }
        a.recycle()
    }

    fun setNormalButton(background: Int, textColor: Int) {
        llCheckLabel!!.setBackgroundResource(background)
        tvCheckLabel!!.setTextColor(textColor)
    }

    fun setTextAllCaps(isAllow: Boolean) {
//        if (tvCheckLabel != null && isAllow){
//            tvCheckLabel.setText(tvCheckLabel.getText().toString().toUpperCase());
//        }
        this.isTextCaps = isAllow
    }

    fun setActive(isActive: Boolean) {
        setCheckButtonBackGround(isActive)
        setCheckButtonTextColor(isActive)
        status = isActive
    }

    fun isActive(): Boolean {
        return status
    }

    fun setLabelButton(text: String) {
        tvCheckLabel!!.text = if (isTextCaps) text.toUpperCase() else text
    }

    open fun setCheckButtonBackGround(isEnable: Boolean) {
        if (isEnable) {
            llCheckLabel!!.background =
                context!!.resources.getDrawable(R.drawable.selector_d7191f_8b0304)
        } else {
            llCheckLabel!!.background =
                context!!.resources.getDrawable(R.drawable.round_solid_e1e5ec_8)
        }
    }

    fun setButtonBackGround(bg: Int) {
        llCheckLabel!!.setBackgroundResource(bg)
    }

    open fun setCheckButtonTextColor(isEnable: Boolean) {
        if (isEnable) {
            tvCheckLabel!!.setTextColor(context!!.resources.getColor(R.color.color_ffffff))
        } else {
            tvCheckLabel!!.setTextColor(context!!.resources.getColor(R.color.color_90a4ae))
        }
    }

    fun isEnable(isUpdate: Boolean, vararg params: String): Boolean { //a,b,c
        return if (isUpdate) {
            if (params[2].isEmpty() || params[3].isEmpty()) {
                setActive(false)
                false
            } else if (params[0] == params[2] && params[1] == params[3]) {
                setActive(false)
                false
            } else {
                setActive(true)
                true
            }
        } else {
            if (params[0].isEmpty() || params[1].isEmpty()) {
                setActive(false)
                false
            } else {
                setActive(true)
                true
            }
        }
    }

    fun isEnable(vararg params: String) { //a,b,c
        val arrayList = ArrayList<String>()
        for (param in params) {
            if (param != "") {
                arrayList.add("T")
            } else {
                arrayList.add("F")
            }
        }
        for (i in arrayList.indices) {
            if (arrayList[i] == "T") {
                setActive(true)
            }
            if (arrayList[i] == "F") {
                setActive(false)
                return
            }
        }
    }
}