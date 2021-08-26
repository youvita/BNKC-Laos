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
import com.bnkc.sourcemodule.R
import com.bnkc.sourcemodule.util.Formats
import java.util.*

class ListChoiceAdapter(
    activity: Context,
    items_list: ArrayList<Any>,
    pos: Int
) :
    BaseAdapter() {
    private val items_list: ArrayList<Any>
    private val layoutInflater: LayoutInflater
    private val context: Context
    private val pos: Int
    override fun getCount(): Int {
        return items_list.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder", "InflateParams")
    override fun getView(
        position: Int,
        convertView: View,
        parent: ViewGroup
    ): View {
        var convertView = convertView
        convertView = layoutInflater.inflate(R.layout.list_choice_item_layout, null)
        val llContainer = convertView.findViewById<LinearLayout>(R.id.ll_12_months)
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
        if (items_list[position] is String) {
            val item = items_list[position] as String
            Log.e(">> ", "item :: $item")
            val checkBox = convertView.findViewById<CheckBox>(R.id.cb_12_months)
            //                        String regex = ".*\\d.*";
//                        boolean containsNumber = item.matches(regex);
//                        if (containsNumber){
//                                checkBox.setTypeface(Utils.getTypeFace(context,"en", 2));
//                                checkBox.setText(FormatUtil.getSeparateFont(context, 2, item), TextView.BufferType.SPANNABLE);
//                        }else{
//                                checkBox.setTypeface(Utils.getTypeFace(context, 2));
//                                checkBox.setText(item);
//                        }
            checkBox.setTypeface(Formats.getTypeFace(context, 2))
            checkBox.text = item
            if (pos == position) {
                checkBox.setTextColor(context.resources.getColor(R.color.color_d7191f))
                llContainer.background =
                    context.getDrawable(R.drawable.round_stroke_d7191f_solid_ffeeee_8)
                checkBox.isChecked = true
            }
        }
//        else if (items_list[position] is AreaRespondObj) {
//            val areaRespondObj: AreaRespondObj = items_list[position] as AreaRespondObj
//            val checkBox = convertView.findViewById<CheckBox>(R.id.cb_12_months)
//            checkBox.setText(areaRespondObj.getAlias1())
//            checkBox.setTypeface(Formats.getTypeFace(context, 2))
//            if (pos == position) {
//                checkBox.setTextColor(context.resources.getColor(R.color.color_d7191f))
//                llContainer.background =
//                    context.getDrawable(R.drawable.round_stroke_d7191f_solid_ffeeee_8)
//                checkBox.isChecked = true
//            }
//        } else if (items_list[position] is ProvinceResData) {
//            val provinceResData: ProvinceResData = items_list[position] as ProvinceResData
//            val checkBox = convertView.findViewById<CheckBox>(R.id.cb_12_months)
//            checkBox.setText(provinceResData.getAlias1())
//            checkBox.setTypeface(Formats.getTypeFace(context, 2))
//            if (pos == position) {
//                checkBox.setTextColor(context.resources.getColor(R.color.color_d7191f))
//                llContainer.background =
//                    context.getDrawable(R.drawable.round_stroke_d7191f_solid_ffeeee_8)
//                checkBox.isChecked = true
//            }
//        }
        return convertView
    }

    init {
        layoutInflater = activity
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        this.items_list = items_list
        context = activity
        this.pos = pos
    }
}
