/**
 * KOSIGN (Cambodia) Investment Co., Ltd.
 * @author chan youvita
 * @since 2021.08.05
 */
package com.bnkc.sourcemodule.binding

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bnkc.sourcemodule.R
import com.bnkc.sourcemodule.ui.ValidateButton
import com.bnkc.sourcemodule.util.FormatUtils
import com.bnkc.sourcemodule.util.Formats
import com.bumptech.glide.Glide
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * binding adapter with layout xml
 */
object BindingAdapters {

    @JvmStatic
    @BindingAdapter("visibleGone")
    fun View.visibleGone(show: Boolean) {
        visibility = if (show) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("visibleInvisible")
    fun View.visibleInvisible(show: Boolean) {
        visibility = if (show) View.VISIBLE else View.INVISIBLE
    }

    @JvmStatic
    @BindingAdapter("textDecimalFormat")
    fun setTextDecimalFormat(tv: TextView, decimal: String?) {
        val format = DecimalFormat(Formats.DECIMAL_FORMAT)
        tv.text = format.format(decimal?.toDouble())
    }

    @JvmStatic
    @BindingAdapter("imageProfileUrl")
    fun setImageProfileUrl(view: ImageView, image: Any?) {
        image?.let {
            Glide
                .with(view.context)
                .load(it)
                .placeholder(R.drawable.ic_avatar_error)
                .error(R.drawable.ic_avatar_error)
                .into(view)
        }
    }

    @JvmStatic
    @BindingAdapter("imageInt")
    fun setImageSrc(view: ImageView, image: Int?) {
        image?.let {
            view.setImageDrawable(ContextCompat.getDrawable(view.context, it))
        }
    }

    @JvmStatic
    @BindingAdapter("textInt")
    fun setTextString(textView: TextView, text: Int?) {
        text?.let {
            textView.text = textView.context.getString(it)
        }
    }

    @JvmStatic
    @BindingAdapter("textDayMonthYearDate")
    fun setDayMonthYear(tvMonthDay: TextView, date: Date?) {
        if (date == null) return
        val format = SimpleDateFormat(Formats.DAY_MONTH_YEAR, Locale.getDefault())
        val hourMinStr = format.format(date)
        tvMonthDay.text = hourMinStr
    }

    @JvmStatic
    @BindingAdapter("textDayMonthYearString")
    fun setDayMonthYearString(tvDayMonth: TextView, dmyString: String?) {
        if (dmyString == null) return
        val format: DateFormat = SimpleDateFormat(Formats.DAY_MONTH_YEAR_FROM, Locale.getDefault())
        val date = format.parse(dmyString) ?: return
        val dayMonthFormat = SimpleDateFormat(Formats.DAY_MONTH_YEAR, Locale.getDefault())
        val dayMonth = dayMonthFormat.format(date)
        tvDayMonth.text = dayMonth
    }

    @JvmStatic
    @BindingAdapter("setNumberFormat")
    fun setNumberFormat(textView: TextView, str: String?) {
        if (str == null) return
        textView.text = FormatUtils.getNumberFormat(textView.context, str)
    }

    @JvmStatic
    @BindingAdapter("setNumberWithoutCurrencyFormat")
    fun setNumberWithoutCurrencyFormat(textView: TextView, str: String?) {
        if (str == null) return
        textView.text = FormatUtils.getNumberFormatWithoutCurrency(textView.context, str)
    }

    @JvmStatic
    @BindingAdapter("currencyFormat")
    fun setCurrencyFormat(textView: TextView, str: String?) {
        textView.text = str?.let {
            FormatUtils.getNumberFormat(textView.context,
                it
            )
        }
    }

    @JvmStatic
    @BindingAdapter("active", "backgroundTextColor")
    fun setActiveOrNormal(view: View, active : Boolean?, bgTextColor : Int?) {
        if (view is TextView){
            if (active!!) {
                view.setTextColor(ContextCompat.getColor(view.context, R.color.color_ffffff))
            } else {
                if (bgTextColor != 0) view.setTextColor(bgTextColor!!)
                else view.setTextColor(ContextCompat.getColor(view.context, R.color.color_90a4ae))
            }
        }else if (view is LinearLayout){
            if (active!!) {
                view.background = ContextCompat.getDrawable(view.context, R.drawable.selector_d7191f_8b0304)
            } else {
                if (bgTextColor != 0)view.background = ContextCompat.getDrawable(view.context, bgTextColor!!)
                else view.background = ContextCompat.getDrawable(view.context, R.drawable.round_solid_e1e5ec_8)
            }
        }

    }

    @JvmStatic
    @BindingAdapter("isSuccess")
    fun ValidateButton.setSuccessOrFailButton(success : Boolean?) {
        status = success!!
        if (!success){
            background = R.drawable.selector_d7191f_ffffee
            textColor = ContextCompat.getColor(context, R.color.colorPrimary)
        }

    }

    @JvmStatic
    @BindingAdapter("label")
    fun setLabelButton(view : ValidateButton,label : String) {
        view.setValue(label)
    }

}