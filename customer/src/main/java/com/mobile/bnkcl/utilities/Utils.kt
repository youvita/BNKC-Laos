package com.mobile.bnkcl.utilities

import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ListView
import android.widget.ScrollView
import androidx.core.content.res.ResourcesCompat
import com.bnkc.library.prefer.CredentialSharedPrefer
import com.bnkc.library.util.Constants
import com.mobile.bnkcl.R

object Utils {
    /*
     * to hide keyboard
     */
    fun setHideKeyboard(context: Context, view: View) {
        try {
            //Set up touch listener for non-text box views to hide keyboard.
            if (!(view is EditText || view is ScrollView)) {
                view.setOnTouchListener { v, event ->
                    val `in` = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    `in`.hideSoftInputFromWindow(v.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                    false
                }
            }

            //If a layout container, iterate over children and seed recursion.
            if (view is ViewGroup) {
                for (i in 0 until view.childCount) {
                    val innerView = view.getChildAt(i)
                    setHideKeyboard(context, innerView)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    /**
     * Get Typeface base on locale
     * @param context
     * @param type 1: regular, 2: medium, 3: bold
     * @return
     */
    fun getTypeFace(context: Context?, type: Int): Typeface? {
        return if (CredentialSharedPrefer.getInstance(context!!).getPrefer(Constants.CHANGED_LANGUAGE).equals("en")) {
            when (type) {
                2 -> ResourcesCompat.getFont(context!!, R.font.rubik_medium)
                3 -> ResourcesCompat.getFont(context!!, R.font.rubik_bold)
                else -> ResourcesCompat.getFont(context!!, R.font.rubik_regular)
            }
        } else {
            when (type) {
                2, 3 -> ResourcesCompat.getFont(context!!, R.font.rubik_bold)
                else -> ResourcesCompat.getFont(context!!, R.font.rubik_bold)
            }
        }
    }

    /**
     * Get Typeface base on locale
     * @param context
     * @param type 1: regular, 2: medium, 3: bold
     * @return
     */
    fun getTypeFace(context: Context?, tag: String, type: Int): Typeface? {
        return if (tag == "en") {
            when (type) {
                2 -> ResourcesCompat.getFont(context!!, R.font.rubik_medium)
                3 -> ResourcesCompat.getFont(context!!, R.font.rubik_bold)
                else -> ResourcesCompat.getFont(context!!, R.font.rubik_regular)
            }
        }
        else {
            when (type) {
                2, 3 -> ResourcesCompat.getFont(context!!, R.font.rubik_bold)
                else -> ResourcesCompat.getFont(context!!, R.font.rubik_medium)
            }
        }
    }

    /*
     * To set show item in ListView
     * calculate list height auto automatically to fit number of child to show in list
     * EX: setListViewHeightBasedOnChildren(listViewSort, 4);
     */
    fun setListViewHeightBasedOnChildren(listView: ListView?, showItem: Int) {
        if (listView == null) {
            return
        }
        val listAdapter = listView.adapter
                ?: // pre-condition
                return
        var totalHeight = listView.paddingTop + listView.paddingBottom
        for (i in 0 until listAdapter.count) {
            if (i > showItem - 1) {
                break
            }
            val listItem = listAdapter.getView(i, null, listView)
            (listItem as? ViewGroup)?.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            listItem.measure(0, 0)
            totalHeight += if (i < listAdapter.count - 1) {
                listItem.measuredHeight + listView.dividerHeight
            } else {
                listItem.measuredHeight - listView.dividerHeight
            }
        }
        val params = listView.layoutParams
        params.height = totalHeight //totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.layoutParams = params
    }

    /*
    * Get quick fill value :: dollar to riels / riels to dollar
    *
    * */
    fun getAmountLoan(TYPE: String, amount: Long): Long {
        return if (TYPE == "USD ") {
            amount
        } else {
            amount * 4000
        }
    }

    fun getRageAmountLoan(TYPE: String, amount: Long): Long {
        return if (TYPE == "USD ") {
            amount + 1
        } else {
            amount * 4000 + 1
        }
    }
}