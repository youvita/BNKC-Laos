package com.bnkc.sourcemodule.util

import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.ListView

fun ListView.setListViewHeightBasedOnChildren(listView : ListView, showItem : Int) {
    var listAdapter: ListAdapter? = listView.adapter
        ?: // pre-condition
        return;

    var totalHeight = listView.paddingTop + listView.paddingBottom;
    for (i in 0..listAdapter!!.count){
        if (i>showItem-1){
            break;
        }
        var listItem = listAdapter.getView(i, null, listView);
        if (listItem is ViewGroup) {
            listItem.setLayoutParams(ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        listItem.measure(0, 0);
        totalHeight += if (i<listAdapter.count -1){
            listItem.measuredHeight + listView.dividerHeight;
        }else {
            listItem.measuredHeight - listView.dividerHeight;
        }
    }

    var params = listView.layoutParams;
    params.height = totalHeight;    //totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
    listView.layoutParams = params;
}