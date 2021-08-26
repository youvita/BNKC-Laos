package com.mobile.bnkcl.ui.main.fragment.office

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bnkc.sourcemodule.base.BaseFragment
import com.bnkc.sourcemodule.dialog.ListChoiceDialog
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.request.findoffice.BranchRequest
import com.mobile.bnkcl.data.response.area.AreaObjResponse
import com.mobile.bnkcl.data.response.dashboard.MyLeasesData
import com.mobile.bnkcl.data.response.office.AreaDataResponse
import com.mobile.bnkcl.databinding.FragmentFindOfficeBinding
import com.mobile.bnkcl.databinding.FragmentMyPageBinding
import com.mobile.bnkcl.ui.adapter.LeaseViewPagerAdapter
import com.mobile.bnkcl.ui.alarm.AlarmActivity
import com.mobile.bnkcl.ui.main.adapter.FindOfficeRecyclerAdapter
import com.mobile.bnkcl.ui.map.MapActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class FindOfficeFragment : BaseFragment<FragmentFindOfficeBinding>() {
    private var recFindOffice: RecyclerView? = null
    private var objects: List<AreaDataResponse>? = null
    private val viewModel : FindOfficeViewModel by viewModels()
    private var areaSelected = 0

//    @Inject
//    lateinit var listChoiceDialog: ListChoiceDialog

    var findOfficeBinding : FragmentFindOfficeBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        findOfficeBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_find_office, container, false)

        binding = findOfficeBinding!!

        return findOfficeBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnNotification.setOnClickListener{
            startActivity(Intent(activity, AlarmActivity::class.java))
        }
//
        val manager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerFindOffice.layoutManager = manager

        viewModel.reqAreasList()
        getAreas()
        getBranches()
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        binding.btnNotification.setOnClickListener{
//            startActivity(Intent(activity, AlarmActivity::class.java))
//        }
////
//        val manager =
//            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//        binding.recyclerFindOffice.layoutManager = manager
//
//        binding.tvAreas.setOnClickListener(View.OnClickListener {
////            if (objects != null && objects!!.size > 0) {
////
////                confirmDialog = ListChoiceDialog.newInstance(
////                    R.drawable.ic_badge_error,
////                    getString(R.string.area),
////                    objects!!,
////                    0
////                )
////                confirmDialog.onConfirmClickedListener {
////
////                }
////                confirmDialog.isCancelable = false
////                confirmDialog.show(activity!!.supportFragmentManager, confirmDialog.tag)
////
//////                DlgLoanTerm.DlgLoanTerm(
//////                    context,
//////                    getString(R.string.area),
//////                    objects,
//////                    areaSelected,
//////                    object : onItemClickLisenter() {
//////                        fun onClickItem(dialogIndex: Int, obj: Any) {
//////                            areaSelected = dialogIndex
//////                            officeBinding!!.tvAreas.text =
//////                                if (obj is AreaRespondObj) (obj as AreaRespondObj).getAlias1() else "not string"
//////                            officeViewModel.setAREA_ID((objects!![areaSelected] as AreaRespondObj).getId())
//////                            officeViewModel.reqBranchList()
//////                        }
//////                    },
//////                    true
//////                )
////            }
//            startActivity(Intent(context, MapActivity::class.java))
//        })
//        viewModel.reqAreasList()
//        getAreas()
//        getBranches()
//    }

//    private fun observeData() {
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
////        officeViewModel.getHandleError().observe(getActivity(),
////            Observer<Any?> { })
//    }

    fun getAreas(){
        viewModel.areaLiveData.observe(requireActivity()) {
//            objects!!.clear()

            val areaRespondObj = AreaObjResponse()
            areaRespondObj.alias1 = getString(R.string.all)
            areaRespondObj.id = 0
//            objects!!.add(areaRespondObj)
//            objects!!.addAll(it)
            viewModel.branchRequest = BranchRequest("", 1, 10, "")
            viewModel.reqBranchList()
        }
    }

    fun getBranches(){
        viewModel.branchLiveData.observe(requireActivity()) {
            val adapter = FindOfficeRecyclerAdapter(it, requireActivity().supportFragmentManager)
            Log.d(">>>>>>>>", "getBranches ${it.size}")
            binding.recyclerFindOffice.adapter = adapter
        }
    }

    fun loanServiceTabSelected() {
//        viewModel.reqAreasList()
    }

    companion object {
        private var firstReq = true
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_find_office
    }
}