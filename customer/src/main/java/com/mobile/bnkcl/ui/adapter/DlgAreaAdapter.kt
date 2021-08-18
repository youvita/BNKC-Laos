package com.mobile.bnkcl.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.LinearLayout
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.response.area.AreaObjResponse
import com.mobile.bnkcl.data.response.area.CapitalData
import com.mobile.bnkcl.utilities.Utils
import com.mobile.bnkcl.utilities.UtilsSize
import java.util.*

class DlgAreaAdapter: BaseAdapter{

    private var items_list:ArrayList<Objects>?  = null
    private var layoutInflater: LayoutInflater? = null
    private var context: Context? = null
    private var position = 0


    constructor(activity: Context, items_list: ArrayList<Objects>, position: Int) {
        this.context = activity
        this.items_list = items_list
        this.position  = position

    }

    override fun getCount(): Int {
       return items_list!!.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
       return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val  view = View.inflate(context, R.layout.item_home_address, null)
        val llContainer = convertView!!.findViewById<LinearLayout>(R.id.ll_12_months)
        if (position == items_list!!.size - 1) {
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            params.setMargins(0, 0, 0, 0)
            llContainer.layoutParams = params
        } else {
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            params.setMargins(0, 0, 0, UtilsSize.getValueInDP(context!!, 10))
            llContainer.layoutParams = params
        }
        if (items_list!![position] is Objects ) {
            val item = items_list!![position] as String
            Log.e(">> ", "item :: $item")
            val checkBox = convertView.findViewById<CheckBox>(R.id.cb_12_months)

            checkBox.setTypeface(Utils.getTypeFace(context, 2))
            checkBox.text = item
            if (position == position) {
                checkBox.setTextColor(context!!.resources.getColor(R.color.color_d7191f))
                llContainer.background = context!!.getDrawable(R.drawable.round_stroke_d7191f_solid_ffeeee_8)
                checkBox.isChecked = true
            }
        } else if (items_list!![position] is Objects) {
            val areaRespondObj: AreaObjResponse = items_list!![position] as AreaObjResponse
            val checkBox = convertView.findViewById<CheckBox>(R.id.cb_12_months)
//            checkBox.setText(areaRespondObj.getAlias1())
            checkBox.setTypeface(Utils.getTypeFace(context, 2))
            if (position == position) {
                checkBox.setTextColor(context!!.resources.getColor(R.color.color_d7191f))
                llContainer.background = context!!.getDrawable(R.drawable.round_stroke_d7191f_solid_ffeeee_8)
                checkBox.isChecked = true
            }
        } else if (items_list!![position] is Objects) {
            val provinceResData: CapitalData = items_list!![position] as CapitalData
            val checkBox = convertView.findViewById<CheckBox>(R.id.cb_12_months)
//            checkBox.setText(provinceResData.getAlias1())
            checkBox.setTypeface(Utils.getTypeFace(context, 2))
            if (position == position) {
                checkBox.setTextColor(context!!.resources.getColor(R.color.color_d7191f))
                llContainer.background = context!!.getDrawable(R.drawable.round_stroke_d7191f_solid_ffeeee_8)
                checkBox.isChecked = true
            }
        }


        return view
    }


}