package com.mobile.bnkcl.ui.main.fragment.office

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.widget.NestedScrollView
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bnkc.library.util.LocaleHelper
import com.bnkc.sourcemodule.base.BaseFragment
import com.mobile.bnkcl.R
import com.mobile.bnkcl.com.view.ShadowLayout
import com.mobile.bnkcl.databinding.FragmentFindOfficeBinding
import com.mobile.bnkcl.utilities.UtilsSize
import java.util.*

class FindOfficeFragment : BaseFragment<FragmentFindOfficeBinding>() {
    private var recFindOffice: RecyclerView? = null
    private var officeBinding: FragmentFindOfficeBinding? = null
    private var officeViewModel: FindOfficeViewModel? = null
    private var objects: ArrayList<Any>? = null
    private var areaSelected = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        return inflater.inflate(R.layout.fragment_find_office, container, false)
//    }

    private fun observeData() {
//        officeViewModel.getBranchLiveData().observe(
//            Objects.requireNonNull(getActivity()),
//            object : Observer<ArrayList<BranchResData?>?> {
//                override fun onChanged(branchResData: ArrayList<BranchResData?>) {
//                    val adapter = FindOfficeRecyclerAdapter(branchResData)
//                    recFindOffice!!.adapter = adapter
//                }
//            })
//        officeViewModel.getAreaLiveData()
//            .observe(getActivity(), object : Observer<ArrayList<AreaRespondObj?>?> {
//                override fun onChanged(areaRespondObjs: ArrayList<AreaRespondObj>) {
//                    if (objects!!.size != areaRespondObjs.size) {
//                        objects!!.clear()
//                        val areaRespondObj = AreaRespondObj()
//                        areaRespondObj.setAlias1(getString(R.string.all))
//                        areaRespondObj.setId(0)
//                        objects!!.add(areaRespondObj)
//                        objects!!.addAll(areaRespondObjs)
//                    }
//                }
//            })
//        officeViewModel.getHandleError().observe(getActivity(),
//            Observer<Any?> { })
    }

    fun loanServiceTabSelected() {
//        if (firstReq) {
//            officeViewModel.reqAreasList()
//            firstReq = false
//        }
    }

    companion object {
        private var firstReq = true
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_find_office
    }
}