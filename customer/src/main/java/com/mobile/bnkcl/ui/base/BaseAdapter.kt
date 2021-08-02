/**
 * KOSIGN (Cambodia) Investment Co., Ltd.
 * @author chan youvita
 * @since 2021.08.01
 */
package com.mobile.bnkcl.ui.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * customize adapter to reusable
 */
abstract class BaseAdapter<B: ViewDataBinding, T ,VH: RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    /** handle items list */
    private var items = ArrayList<T>()

    /** handle item binding */
    lateinit var binding: B

    /** handle item layout */
    abstract fun getLayoutId(viewType: Int): Int

    /** handle custom view holder */
    abstract fun setViewHolder(parent: ViewGroup): VH

    /** handle bind data */
    abstract fun setBindData(holder: VH, data: T, position: Int)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(inflater, getLayoutId(viewType), parent, false)
        return setViewHolder(parent)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        setBindData(holder, items[position], position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    /**
     * add all items to array list view
     * @param data items list
     */
    open fun addItemList(data: List<T>?) {
        if (data == null) return
        items.clear()
        items.addAll(data)
        this.notifyItemInserted(itemCount)
    }

}