package com.bnkcl.employeemodule.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.viewpager2.widget.ViewPager2
import com.bnkc.employee.R
import com.bnkc.employee.databinding.NavBottomMenuLayoutBinding
import com.bnkc.sourcemodule.base.BaseActivity
import com.bnkc.sourcemodule.databinding.TabItemViewBinding
import com.bnkc.sourcemodule.ui.TabViewPagerAdapter
import com.bnkcl.employeemodule.ui.check.CheckListFragment
import com.bnkcl.employeemodule.ui.find.FindCustomerFragment
import com.bnkcl.employeemodule.ui.notice.NoticeFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class EmployeeActivity: BaseActivity<NavBottomMenuLayoutBinding>() {

    private var tabLayout: TabLayout? = null

    private var viewPager: ViewPager2? = null

    private var tabAdapter: TabViewPagerAdapter = TabViewPagerAdapter(supportFragmentManager, lifecycle)

    /**
     * menu title list
     */
    private val menuNames = arrayOf(
        R.string.menu_nav_notice,
        R.string.menu_find_customer,
        R.string.menu_check_list)

    /**
     * menu icons list
     */
    private val menuIcons = arrayOf(
        R.drawable.selector_tab_notice,
        R.drawable.selector_tab_find_customer,
        R.drawable.selector_tab_check_list)

    override fun getLayoutId(): Int = R.layout.nav_bottom_menu_layout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewPager = binding.viewPager
        tabLayout = binding.tabBottomMenu.mainTabLayout

        tabAdapter.addFragment(NoticeFragment())
        tabAdapter.addFragment(FindCustomerFragment())
        tabAdapter.addFragment(CheckListFragment())

        viewPager?.adapter = tabAdapter

        TabLayoutMediator(tabLayout!!, viewPager!!) { tab, position ->
            val binding = TabItemViewBinding.inflate(LayoutInflater.from(this))
            binding.menuName = menuNames[position]
            binding.menuIcon = menuIcons[position]
            tab.customView = binding.root
        }.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabLayout = null
        viewPager = null
    }
}