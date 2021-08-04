package com.mobile.bnkcl.com.view

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.AttributeSet
import android.view.View
import android.widget.*
import androidx.annotation.IntDef
import com.mobile.bnkcl.R

class ActionBar : RelativeLayout, View.OnClickListener {
    private var mContext: Context
    private var actionBarImgLeft: ImageView? = null
    private var actionBarImgRight: ImageView? = null
    private var actionBarTextLeft: TextView? = null
    private var actionBarTextRight: TextView? = null
    private var actionBarText: TextView? = null
    private var rlRoot: RelativeLayout? = null
    private var lfActionBarLeft: FrameLayout? = null
    private var lfActionBarRight: FrameLayout? = null

    @IntDef(value = [ONLY_LEFT, ONLY_RIGHT])
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    internal annotation class Only {}

    constructor(context: Context) : super(context) {
        mContext = context
        initLayout()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        mContext = context
        initLayout()
    }

    private fun initLayout() {
        val view = inflate(mContext, R.layout.comm_action_bar, null)
        addView(view, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
        actionBarImgLeft = view.findViewById(R.id.id_action_bar_left)
        actionBarImgRight = view.findViewById(R.id.id_action_bar_right)
        actionBarTextLeft = view.findViewById(R.id.id_action_bar_text_left)
        actionBarTextRight = view.findViewById(R.id.id_action_bar_text_right)
        actionBarText = view.findViewById(R.id.id_action_bar_text)
        rlRoot = view.findViewById(R.id.rl_root)
        lfActionBarRight = view.findViewById(R.id.lf_action_bar_right)
        lfActionBarLeft = view.findViewById(R.id.lf_action_bar_left)
    }

    private val activity: Activity
        get() {
            var ctx = context
            while (ctx is ContextWrapper) {
                if (ctx is Activity) {
                    return ctx
                }
                ctx = ctx.baseContext
            }
            return mContext as Activity
        }

    fun setToolbarTitleWithActionBack(img: Int, title: String?): ActionBar {
        actionBarText!!.visibility = GONE
        lfActionBarLeft!!.visibility = VISIBLE
        lfActionBarRight!!.visibility = INVISIBLE
        actionBarImgLeft!!.setImageResource(img)
        actionBarTextLeft!!.visibility = VISIBLE
        actionBarTextLeft!!.text = title
        return this
    }

    fun setToolbarTitleWithActionBack(title: String?): ActionBar {
        actionBarText!!.visibility = GONE
        lfActionBarLeft!!.visibility = VISIBLE
        lfActionBarRight!!.visibility = INVISIBLE
        actionBarTextLeft!!.visibility = VISIBLE
        actionBarTextLeft!!.text = title
        return this
    }

    fun setRightImageResourceShowing(img: Int, isShowing: Boolean): ActionBar {
        if (isShowing) {
            lfActionBarRight!!.visibility = VISIBLE
            actionBarImgRight!!.visibility = VISIBLE
            actionBarImgRight!!.setImageResource(img)
        }
        actionBarTextRight!!.visibility = GONE
        return this
    }

    fun setRightImageResourceShowing(isShowing: Boolean): ActionBar {
        if (isShowing) {
            lfActionBarRight!!.visibility = VISIBLE
            actionBarImgRight!!.visibility = VISIBLE
        }
        actionBarTextRight!!.visibility = GONE
        return this
    }

    fun setToolbarTitleLeft(title: String?): ActionBar {
        lfActionBarLeft!!.visibility = GONE
        actionBarText!!.text = title
        return this
    }

    fun setLeftImageResource(image: Int): ActionBar {
        actionBarImgLeft!!.visibility = VISIBLE
        actionBarImgLeft!!.setImageResource(image)
        actionBarTextLeft!!.visibility = GONE
        return this
    }

    fun setRightImageResource(image: Int): ActionBar {
        actionBarImgRight!!.visibility = VISIBLE
        actionBarImgRight!!.setImageResource(image)
        actionBarTextRight!!.visibility = GONE
        return this
    }

    /**
     * Show which Left or Right
     * @see .ONLY_LEFT
     *
     * @see .ONLY_RIGHT
     */
    fun setSingleMenu(@Only leftRight: Int): ActionBar {
        if (leftRight == ONLY_LEFT) {
            lfActionBarLeft!!.visibility = VISIBLE
            lfActionBarRight!!.visibility = INVISIBLE
        } else {
            lfActionBarLeft!!.visibility = INVISIBLE
        }
        return this
    }

    fun setText(text: String?): ActionBar {
        actionBarText!!.text = text
        return this
    }

    fun setBackgroundActionBar(color: Int): ActionBar {
        rlRoot!!.setBackgroundColor(color)
        return this
    }

    fun setTextLeft(text: String?): ActionBar {
        actionBarImgLeft!!.visibility = GONE
        actionBarTextLeft!!.text = text
        actionBarTextLeft!!.visibility = VISIBLE
        return this
    }

    fun setTextLeft(text: String?, isEnable: Boolean): ActionBar {
        actionBarImgLeft!!.visibility = GONE
        actionBarTextLeft!!.text = text
        actionBarTextLeft!!.visibility = VISIBLE
        setEnableTextLeft(isEnable)
        return this
    }

    fun setTextRight(text: String?): ActionBar {
        actionBarImgRight!!.visibility = GONE
        actionBarTextRight!!.text = text
        actionBarTextRight!!.visibility = VISIBLE
        return this
    }

    fun setTextRight(text: String?, isEnable: Boolean): ActionBar {
        actionBarImgRight!!.visibility = GONE
        actionBarTextRight!!.text = text
        actionBarTextRight!!.visibility = VISIBLE
        setEnableTextRight(isEnable)
        return this
    }

    private fun setEnableTextRight(check: Boolean): ActionBar {
        actionBarTextRight!!.isEnabled = check
        return this
    }

    private fun setEnableTextLeft(check: Boolean): ActionBar {
        actionBarTextLeft!!.isEnabled = check
        return this
    }

    fun setOnMenuLeftClick(onClickListener: OnClickListener?): ActionBar {
        if (actionBarTextLeft!!.visibility == VISIBLE) {
            actionBarImgLeft!!.setOnClickListener(onClickListener)
            //            actionBarTextLeft.setOnClickListener(onClickListener);
        } else {
            actionBarImgLeft!!.setOnClickListener(onClickListener)
        }
        return this
    }

    fun setOnMenuRightClick(onClickListener: OnClickListener?): ActionBar {
        if (actionBarTextRight!!.visibility == VISIBLE) {
            actionBarImgRight!!.setOnClickListener(onClickListener)
        } else {
            actionBarImgRight!!.setOnClickListener(onClickListener)
        }
        return this
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.id_action_bar_text_left, R.id.id_action_bar_left -> activity.finish()
        }
    }

    companion object {
        const val ONLY_LEFT = 0
        const val ONLY_RIGHT = 1
        fun applyTo(context: Context, v: LinearLayout): ActionBar {
            val actionBar = ActionBar(context, null)
            v.addView(actionBar, 0)
            return actionBar
        }
    }
}