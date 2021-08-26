package com.bnkc.sourcemodule.util

import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.ListView

fun ListView.setListViewHeightBasedOnChildren(listView : ListView, showItem : Int) {
    if (listView == null) {
        return;
    }
    var listAdapter = listView.getAdapter();
    if (listAdapter == null) {
        // pre-condition
        return;
    }

    var totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
    for (i in 0..listAdapter.count){
        if (i>showItem-1){
            break;
        }
        var listItem = listAdapter.getView(i, null, listView);
        if (listItem is ViewGroup) {
            listItem.setLayoutParams(ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        listItem.measure(0, 0);
        if (i<listAdapter.getCount()-1){
            totalHeight += listItem.getMeasuredHeight() + listView.getDividerHeight();
        }else {
            totalHeight += listItem.getMeasuredHeight() - listView.getDividerHeight();
        }
    }

    var params = listView.layoutParams;
    params.height = totalHeight;    //totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
    listView.layoutParams = params;
}