package com.bnkc.sourcemodule.binding

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bnkc.sourcemodule.R
import com.bnkc.sourcemodule.ui.ValidateButton
import com.bnkc.sourcemodule.util.FormatUtils
import org.w3c.dom.Text

object ValidateButtonBindingAdapter {

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

}