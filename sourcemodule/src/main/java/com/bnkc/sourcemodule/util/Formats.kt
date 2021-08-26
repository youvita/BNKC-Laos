package com.bnkc.sourcemodule.util

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import androidx.core.content.res.ResourcesCompat
import com.bnkc.sourcemodule.R

object Formats {

    // Date
    const val DAY_MONTH_YEAR = "dd-MM-yyyy"
    const val DAY_MONTH_YEAR_FROM = "yyyyMMdd"

    // Decimal format
    const val DECIMAL_FORMAT = "#,###,###"

    fun getSeparateFontByLang(
        mContext: Context,
        indexStart: Int,
        indexEnd: Int,
        text: String?,
        isClicked: Boolean
    ): SpannableString? {
        val ss = SpannableString(text)
        if (isClicked) {
            ss.setSpan(
                getTypeFace(mContext, 2)?.let { CustomTypefaceSpan(it) },
                indexStart,
                indexEnd,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            ss.setSpan(
                ForegroundColorSpan(mContext.resources.getColor(R.color.color_d7191f)),
                indexStart,
                indexEnd,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        } else {
            ss.setSpan(
                getTypeFace(mContext, 2)?.let { CustomTypefaceSpan(it) },
                indexStart,
                indexEnd,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            ss.setSpan(
                ForegroundColorSpan(mContext.resources.getColor(R.color.color_d7191f)),
                indexStart,
                indexEnd,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        return ss
    }

    /**
     * Get Typeface base on locale
     * @param context
     * @param type 1: regular, 2: medium, 3: bold
     * @return
     */
    fun getTypeFace(context: Context?, type: Int): Typeface? {
        return when (type) {
            2 -> ResourcesCompat.getFont(
                context!!,
                R.font.rubik_medium
            )
            3 -> ResourcesCompat.getFont(
                context!!,
                R.font.rubik_bold
            )
            else -> ResourcesCompat.getFont(
                context!!,
                R.font.rubik_regular
            )
        }
    }

    fun getValueInDP(context: Context, value: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            value.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }
}