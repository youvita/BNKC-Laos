package com.bnkcl.employeemodule.ui

import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.bnkc.employee.R
import com.bnkc.employee.databinding.NavBottomMenuLayoutBinding
import com.bnkc.sourcemodule.base.BaseActivity
import com.bnkc.sourcemodule.ui.TabViewPagerAdapter
import com.bnkcl.employeemodule.ui.check.CheckListFragment
import com.bnkcl.employeemodule.ui.find.FindCustomerFragment
import com.bnkcl.employeemodule.ui.notice.NoticeFragment
import com.google.android.material.tabs.TabLayout

class EmployeeActivity: BaseActivity<NavBottomMenuLayoutBinding>() {

    override fun getLayoutId(): Int = R.layout.nav_bottom_menu_layout

//    val navView: BottomNavigationView by lazy { findViewById(R.id.navView) }
    private var navView: TabLayout? = null
    private var mainViewPager: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        setSupportActionBar(binding.toolbar)

        mainViewPager = binding.mainViewPager
        navView = binding.tabBottomMenu.mainTabLayout

        val tabViewPagerAdapter = TabViewPagerAdapter(supportFragmentManager, this)
        val noticeFragment = NoticeFragment()
        val findCustomerFragment = FindCustomerFragment()
        val checkListFragment = CheckListFragment()

        tabViewPagerAdapter.addFragment(noticeFragment, "Notice", R.drawable.selector_tab_notice)
        tabViewPagerAdapter.addFragment(findCustomerFragment, "Find Customer", R.drawable.selector_tab_find_customer)
        tabViewPagerAdapter.addFragment(checkListFragment, "Check List", R.drawable.selector_tab_check_list)

        mainViewPager?.adapter = tabViewPagerAdapter
        mainViewPager!!.offscreenPageLimit = 3
        navView!!.setupWithViewPager(mainViewPager)
        for (i in 0 until navView!!.getTabCount()) {
            navView!!.getTabAt(i)!!.customView = tabViewPagerAdapter!!.getTabView(i)
        }
    }
}