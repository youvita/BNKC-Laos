package com.mobile.bnkcl.comm.view

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.IntDef
import com.mobile.bnkcl.R
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

class ActionBar : RelativeLayout, View.OnClickListener {

    private val mContext: Context? = null
    private var actionBarImgLeft: ImageView? = null
    private var actionBarImgRight: ImageView? = null
    private var actionBarTextLeft: TextView? = null
    private var actionBarTextRight: TextView? = null
    private var actionBarText: TextView? = null
    private var rl_root: RelativeLayout? = null
    private var lfActionBarLeft: FrameLayout? = null
    private  var lfActionBarRight:FrameLayout? = null
    companion object {
        const val ONLY_LEFT = 0
        const val ONLY_RIGHT = 1
    }

    @IntDef(ONLY_LEFT, ONLY_RIGHT)
    @Retention(RetentionPolicy.SOURCE)
    internal annotation class Only {}



    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)


    private fun initLayout() {
//        val view = inflate(mContext, R.layout.comm_action_bar, null)
//        addView(view, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
//        actionBarImgLeft = view.findViewById(R.id.id_action_bar_left)
//        actionBarImgRight = view.findViewById(R.id.id_action_bar_right)
//        actionBarTextLeft = view.findViewById(R.id.id_action_bar_text_left)
//        actionBarTextRight = view.findViewById(R.id.id_action_bar_text_right)
//        actionBarText = view.findViewById(R.id.id_action_bar_text)
//        rl_root = view.findViewById(R.id.rl_root)
//        lfActionBarRight = view.findViewById(R.id.lf_action_bar_right)
//        lfActionBarLeft = view.findViewById(R.id.lf_action_bar_left)
//        actionBarImgLeft.setOnClickListener(this)
//        actionBarTextLeft.setOnClickListener(this);
    }

    private fun getActivity(): Activity? {
        var ctx = context
        while (ctx is ContextWrapper) {
            if (ctx is Activity) {
                return ctx
            }
            ctx = ctx.baseContext
        }
        return mContext as Activity?
    }

    fun setToolbarTitleWithActionBack(
        img: Int,
        title: String?
    ): com.mobile.bnkcl.comm.view.ActionBar {
        actionBarText!!.visibility = GONE
        lfActionBarLeft!!.visibility = VISIBLE
        lfActionBarRight!!.visibility = INVISIBLE
        //        lfActionBarLeft.setVisibility(INVISIBLE);
        actionBarImgLeft!!.setImageResource(img)
        actionBarTextLeft!!.visibility = VISIBLE
        actionBarTextLeft!!.text = title
        return this
    }

    fun setToolbarTitleWithActionBack(title: String?): com.mobile.bnkcl.comm.view.ActionBar {
        actionBarText!!.visibility = GONE
        lfActionBarLeft!!.visibility = VISIBLE
        lfActionBarRight!!.visibility = INVISIBLE
        //        lfActionBarLeft.setVisibility(INVISIBLE);
        actionBarTextLeft!!.visibility = VISIBLE
        actionBarTextLeft!!.text = title

        return this
    }

    fun setText(text: String?): com.mobile.bnkcl.comm.view.ActionBar {
        actionBarText!!.text = text
        return this
    }

    fun setBackgroundActionBar(color: Int): com.mobile.bnkcl.comm.view.ActionBar{
        rl_root!!.setBackgroundColor(color)
        return this
    }

    fun setEnableTextRight(check: Boolean): com.mobile.bnkcl.comm.view.ActionBar{
        if (!check) {
            actionBarTextRight!!.isEnabled = false
        } else {
            actionBarTextRight!!.isEnabled = true
        }
        return this
    }

    fun setEnableTextLeft(check: Boolean): com.mobile.bnkcl.comm.view.ActionBar {
        if (!check) {
            actionBarTextLeft!!.isEnabled = false
        } else {
            actionBarTextLeft!!.isEnabled = true
        }
        return this
    }

    // setOnClickListener
    fun setOnMenuLeftClick(onClickListener: OnClickListener?): com.mobile.bnkcl.comm.view.ActionBar {
        if (actionBarTextLeft!!.visibility == VISIBLE) {
            actionBarImgLeft!!.setOnClickListener(onClickListener)

        } else {
            actionBarImgLeft!!.setOnClickListener(onClickListener)
        }
        return this
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
//            R.id.id_action_bar_text_left, R.id.id_action_bar_left -> getActivity()!!.finish()
        }    }


}