package com.bnkc.sourcemodule.dialog.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.bnkc.sourcemodule.R
import com.bnkc.sourcemodule.databinding.ListChoiceItemLayoutBinding
import com.bnkc.sourcemodule.util.Formats
import java.util.*

class ListChoiceAdapter(
    activity: Context,
    private val items_list: ArrayList<String>,
    private val pos: Int
) :
    BaseAdapter() {

    private val inflater: LayoutInflater
            = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private val context: Context = activity


    override fun getCount(): Int {
        return items_list.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Get view for row item
        val itemBinding = ListChoiceItemLayoutBinding.inflate(inflater)
//        val llContainer = rowView.findViewById<LinearLayout>(R.id.ll_12_months)
        if (position == items_list.size - 1) {
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            params.setMargins(0, 0, 0, 0)
            itemBinding.ll12Months.layoutParams = params
        } else {
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            params.setMargins(0, 0, 0, Formats.getValueInDP(context, 10))
            itemBinding.ll12Months.layoutParams = params
        }
        val item = items_list[position]
        Log.e(">> ", "item :: $item")
        itemBinding.cb12Months.typeface = Formats.getTypeFace(context, 2)
        itemBinding.cb12Months.text = item
        if (pos == position) {
            itemBinding.cb12Months.setTextColor(ContextCompat.getColor(context, R.color.color_d7191f))
            itemBinding.ll12Months.background =
                ContextCompat.getDrawable(context ,R.drawable.round_stroke_d7191f_solid_ffeeee_8)
            itemBinding.cb12Months.isChecked = true
        }
        return itemBinding.root
    }

}
