package com.bnkc.sourcemodule.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.bnkc.sourcemodule.R
import java.util.ArrayList

class TabViewPagerAdapter(fm: FragmentManager?, private val mContext: Context) : FragmentStatePagerAdapter(fm!!) {

    private val mFragmentList: ArrayList<Fragment>
    private val mTitleList: ArrayList<String>
    private val mIconList: ArrayList<Int>
    fun addFragment(fragment: Fragment, title: String, icon: Int) {
        mFragmentList.add(fragment)
        mTitleList.add(title)
        mIconList.add(icon)
    }

    /**
     * Return fragment based on the position.
     * @param position Position
     * @return Fragment
     */
    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mTitleList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    fun getTabView(position: Int): View {
        val view: View = LayoutInflater.from(mContext).inflate(R.layout.tab_item_view, null)
        (view.findViewById<View>(R.id.icon) as ImageView).setImageResource(
                mIconList[position]
        )
        (view.findViewById<View>(R.id.title) as TextView).text = mTitleList[position]
        return view
    }

    /**
     * Constructor
     * @param fm FragmentManager
     */
    init {
        mFragmentList = ArrayList()
        mTitleList = ArrayList()
        mIconList = ArrayList()
    }
}