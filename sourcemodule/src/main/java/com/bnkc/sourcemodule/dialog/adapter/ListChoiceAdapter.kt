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
        val rowView = inflater.inflate(R.layout.list_choice_item_layout, parent, false)
        val llContainer = rowView.findViewById<LinearLayout>(R.id.ll_12_months)
        if (position == items_list.size - 1) {
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            params.setMargins(0, 0, 0, 0)
            llContainer.layoutParams = params
        } else {
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            params.setMargins(0, 0, 0, Formats.getValueInDP(context, 10))
            llContainer.layoutParams = params
        }
        val item = items_list[position]
        Log.e(">> ", "item :: $item")
        val checkBox = rowView.findViewById<CheckBox>(R.id.cb_12_months)
        checkBox.typeface = Formats.getTypeFace(context, 2)
        checkBox.text = item
        if (pos == position) {
            checkBox.setTextColor(context.resources.getColor(R.color.color_d7191f))
            llContainer.background =
                ContextCompat.getDrawable(context ,R.drawable.round_stroke_d7191f_solid_ffeeee_8)
            checkBox.isChecked = true
        }
        return rowView
    }

//    override fun getView(
//        position: Int,
//        convertView: View?,
//        parent: ViewGroup
//    ): View {
//        convertView  = inflater.inflate(R.layout.list_choice_item_layout, null)
//        val llContainer = convertView.findViewById<LinearLayout>(R.id.ll_12_months)
//        if (position == items_list.size - 1) {
//            val params = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT
//            )
//            params.setMargins(0, 0, 0, 0)
//            llContainer.layoutParams = params
//        } else {
//            val params = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT
//            )
//            params.setMargins(0, 0, 0, Formats.getValueInDP(context, 10))
//            llContainer.layoutParams = params
//        }
//        val item = items_list[position]
//        Log.e(">> ", "item :: $item")
//        val checkBox = convertView.findViewById<CheckBox>(R.id.cb_12_months)
//        checkBox.typeface = Formats.getTypeFace(context, 2)
//        checkBox.text = item
//        if (pos == position) {
//            checkBox.setTextColor(context.resources.getColor(R.color.color_d7191f))
//            llContainer.background =
//                ContextCompat.getDrawable(context ,R.drawable.round_stroke_d7191f_solid_ffeeee_8)
//            checkBox.isChecked = true
//        }
//        return convertView
//    }

}
