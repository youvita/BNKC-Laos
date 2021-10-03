package com.mobile.bnkcl.util

import android.content.Context
import android.util.TypedValue
import android.view.View

object UtilsSize {
    var meta_factor = 0f

    /*
    * Get screen width
    * easy to perform dynamic view
    * */
    fun getScreenWidth(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }

    fun getValueInDP(context: Context, value: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            value.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }

    fun hasLineBreak(context: Context): Boolean {
        return if (getScreenWidth(context) < 758) {
            meta_factor = 3f
            true
        } else if (getScreenWidth(context) < 1280) {
            meta_factor = 2f
            true
        } else if (getScreenWidth(context) < 1441) {
            meta_factor = 1.5f
            true
        } else {
            meta_factor = 1f
            false
        }
    }

    fun getExpandHeight(v: View): Int {
        val matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(
            v.width,
            View.MeasureSpec.EXACTLY
        )
        val wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(
            0,
            View.MeasureSpec.UNSPECIFIED
        )
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec)
        return v.measuredHeight
    }
}