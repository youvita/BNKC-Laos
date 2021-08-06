//package com.mobile.bnkcl.ui.main.fragment.office
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.coordinatorlayout.widget.CoordinatorLayout
//import androidx.core.widget.NestedScrollView
//import androidx.databinding.library.baseAdapters.BR
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.Observer
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.mobile.bnkcl.R
//import com.mobile.bnkcl.utilities.UtilsSize
//import java.util.*
//
//class FindOfficeFragment : Fragment() {
//    private var recFindOffice: RecyclerView? = null
//    private var officeBinding: FragmentFindOfficeBinding? = null
//    private var officeViewModel: FindOfficeViewModel? = null
//    private var objects: ArrayList<Any>? = null
//    private var areaSelected = 0
//    fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        objects = ArrayList()
//    }
//
//    fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        // Inflate the layout for this fragment
//        officeViewModel =
//            FindOfficeViewModel(Objects.requireNonNull(getActivity()).getApplication())
//        setFragmentViewBinding(
//            inflater,
//            R.layout.fragment_find_office,
//            container,
//            officeViewModel,
//            BR._all
//        )
//        officeBinding = mViewDataBinding as FragmentFindOfficeBinding?
//        (getActivity() as MainActivity).setViewModelObserver(officeViewModel)
//        (getActivity() as MainActivity).setLoadingObserver()
//        val nestedScrollView: NestedScrollView = officeBinding.nsvFindOffice
//        val tvAreas: TextView = officeBinding.tvAreas
//        val slArea: ShadowLayout = officeBinding.slAreas
//        val params = CoordinatorLayout.LayoutParams(
//            CoordinatorLayout.LayoutParams.MATCH_PARENT,
//            CoordinatorLayout.LayoutParams.MATCH_PARENT
//        )
//        if (LocaleHelper.getLanguage(getContext()).equals(Code.LanguageCode.KHMER)) {
//            params.setMargins(0, UtilsSize.getValueInDP(getContext(), 80), 0, 0)
//            nestedScrollView.layoutParams =
//                params // note that textview would be your instanced TextView object
//        } else {
//            params.setMargins(0, UtilsSize.getValueInDP(getContext(), 68), 0, 0)
//            nestedScrollView.layoutParams =
//                params // note that textview would be your instanced TextView object
//        }
//        val params1 = CoordinatorLayout.LayoutParams(
//            CoordinatorLayout.LayoutParams.MATCH_PARENT,
//            CoordinatorLayout.LayoutParams.WRAP_CONTENT
//        )
//        if (LocaleHelper.getLanguage(getContext()).equals(Code.LanguageCode.KHMER)) {
//            params1.setMargins(
//                UtilsSize.getValueInDP(getContext(), 10),
//                UtilsSize.getValueInDP(getContext(), 35),
//                UtilsSize.getValueInDP(getContext(), 10),
//                0
//            )
//            slArea.setLayoutParams(params1) // note that textview would be your instanced TextView object
//        } else {
//            params1.setMargins(
//                UtilsSize.getValueInDP(getContext(), 10),
//                UtilsSize.getValueInDP(getContext(), 29),
//                UtilsSize.getValueInDP(getContext(), 10),
//                0
//            )
//            slArea.setLayoutParams(params1) // note that textview would be your instanced TextView object
//        }
//        recFindOffice = officeBinding.recyclerFindOffice
//        val manager = LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false)
//        recFindOffice!!.layoutManager = manager
//        slArea.setOnClickListener(View.OnClickListener {
//            if (objects != null && objects!!.size > 0) {
//                DlgLoanTerm.DlgLoanTerm(
//                    getContext(),
//                    getString(R.string.area),
//                    objects,
//                    areaSelected,
//                    object : onItemClickLisenter() {
//                        fun onClickItem(dialogIndex: Int, obj: Any) {
//                            areaSelected = dialogIndex
//                            officeBinding.tvAreas.setText(if (obj is AreaRespondObj) (obj as AreaRespondObj).getAlias1() else "not string")
//                            officeViewModel.setAREA_ID((objects!![areaSelected] as AreaRespondObj).getId())
//                            officeViewModel.reqBranchList()
//                        }
//                    },
//                    true
//                )
//            }
//        })
//        observeData()
//        return officeBinding.getRoot()
//    }
//
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
//        officeViewModel.getHandleError().observe(getActivity(),
//            Observer<Any?> { })
//    }
//
//    fun loanServiceTabSelected() {
//        if (firstReq) {
//            officeViewModel.reqAreasList()
//            firstReq = false
//        }
//    }
//
//    fun onDestroyView() {
//        super.onDestroyView()
//        firstReq = true
//        objects!!.clear()
//    }
//
//    companion object {
//        private var firstReq = true
//    }
//}