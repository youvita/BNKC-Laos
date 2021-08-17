/**
 * KOSIGN (Cambodia) Investment Co., Ltd.
 * @author chan youvita
 * @since 2021.08.05
 */
package com.bnkc.sourcemodule.binding

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bnkc.sourcemodule.R
import com.bnkc.sourcemodule.ui.ValidateButton
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
    @BindingAdapter("text", "isChecked")
    fun setUpAgreementText(textView: TextView, txtAgreement: String?, checked: Boolean) {
        var txtAgreement = "I agree to BNKC\'s Terms and Conditions"
        Log.d(">>>>>", "setUpAgreementText $txtAgreement $checked")
        textView.setTextColor(textView.context.resources.getColor(R.color.color_263238))
        textView.setText(
            Formats.getSeparateFontByLang(
                textView.context,
                18,
                txtAgreement!!.length,
                txtAgreement,
                checked
            ), TextView.BufferType.SPANNABLE
        )
    }

    @JvmStatic
    @BindingAdapter("textButton")
    fun textButton(textView: ValidateButton, str: String?) {
        textView.tvCheckLabel!!.text = str
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @JvmStatic
    @BindingAdapter("enableButton")
    fun ValidateButton.enableButton(isEnable: Boolean?) {
        if (isEnable != null && isEnable) {
            Log.d(">>>>>", "BindingAdapter $isEnable")
            llCheckLabel!!.background =
                context!!.resources.getDrawable(R.drawable.selector_d7191f_8b0304)
            tvCheckLabel!!.setTextColor(context!!.resources.getColor(R.color.color_ffffff))
        } else {
            Log.d(">>>>>", "BindingAdapter $isEnable")
            llCheckLabel!!.background =
                context!!.resources.getDrawable(R.drawable.round_solid_e1e5ec_8)
            tvCheckLabel!!.setTextColor(context!!.resources.getColor(R.color.color_90a4ae))
            setOnClickListener(null)
        }

    }

}