package com.mobile.bnkcl.com.view.pincode

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import com.mobile.bnkcl.R
import com.mobile.bnkcl.util.UtilsSize.getScreenWidth

import java.util.*

class PinCodeView : LinearLayout {
    var mCodeViews: MutableList<CheckBox> = ArrayList()
    var code = ""
        private set
    private var mCodeLength = DEFAULT_CODE_LENGTH
    private var mListener: OnPFCodeListener? = null
    private var screenWidth = 0

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
        init()
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.view_code, this)
        setUpCodeViews()
    }

    fun setCodeLength(codeLength: Int) {
        mCodeLength = codeLength
        setUpCodeViews()
    }

    private fun setUpCodeViews() {
        removeAllViews()
        mCodeViews.clear()
        this.code = ""
        //Scale width by screen size dynamically
        screenWidth = if (mCodeLength > 7) {
            getScreenWidth(context) / (mCodeLength + 3)
        } else if (mCodeLength > 5) {
            getScreenWidth(context) / (mCodeLength + 2)
        } else {
            getScreenWidth(context) / (mCodeLength + 1)
        }
        for (i in 0 until mCodeLength) {
            val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.view_code_state, null) as CheckBox
            view.buttonDrawable = makeSelector(context, screenWidth)
            val layoutParams = LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            val margin = resources.getDimensionPixelSize(R.dimen.code_fp_margin);
            layoutParams.setMargins(margin, margin, margin, margin)
            view.layoutParams = layoutParams
            view.isChecked = false
            addView(view)
            mCodeViews.add(view)
        }
        if (mListener != null) {
            mListener!!.onCodeNotCompleted("")
        }
    }



    fun input(number: String): Int {
        if (code.length == mCodeLength) {
            return code.length
        }
        mCodeViews[code.length].toggle() //.setChecked(true);
        this.code += number
        if (code.length == mCodeLength && mListener != null) {
            mListener!!.onCodeCompleted(this.code)
        }
        return code.length
    }

    fun delete(): Int {
        if (mListener != null) {
            mListener!!.onCodeNotCompleted(this.code)
        }
        if (code.length == 0) {
            return code.length
        }
        this.code = code.substring(0, code.length - 1)
        mCodeViews[code.length].toggle() //.setChecked(false);
        return code.length
    }

    fun clearCode() {
        if (mListener != null) {
            mListener!!.onCodeNotCompleted(this.code)
        }
        this.code = ""
        for (codeView in mCodeViews) {
            codeView.isChecked = false
        }
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
    }

    val inputCodeLength: Int
        get() = code.length

    fun setListener(listener: OnPFCodeListener?) {
        mListener = listener
    }

    interface OnPFCodeListener {
        fun onCodeCompleted(code: String?)
        fun onCodeNotCompleted(code: String?)
    }

    companion object {
        private const val DEFAULT_CODE_LENGTH = 4
        fun makeSelector(
            context: Context,
            w: Int
        ): StateListDrawable {
            val res =
                StateListDrawable()
            res.setExitFadeDuration(200)
            res.addState(
                intArrayOf(android.R.attr.state_checked),
                createPressedDrawable(
                    w,
                    context.resources.getColor(R.color.color_ffffff),
                    context.resources.getColor(R.color.color_1B252A),
                    w / 3 - 5
                )
            )
            res.addState(
                intArrayOf(),
                createPressedDrawable(
                    w,
                    context.resources.getColor(R.color.color_1B252A)
                )
            )
            return res
        }

        fun createPressedDrawable(
            w: Int,
            sldColor: Int,
            strColor: Int,
            strSize: Int
        ): GradientDrawable {
            val shape = GradientDrawable()
            shape.shape = GradientDrawable.OVAL
            shape.setSize(w, w)
            shape.setColor(sldColor)
            shape.setStroke(strSize, strColor)
            return shape
        }

        fun createPressedDrawable(w: Int, sldColor: Int): GradientDrawable {
            val shape = GradientDrawable()
            shape.shape = GradientDrawable.OVAL
            shape.setSize(w, w)
            shape.setColor(sldColor)
            return shape
        }
    }
}
