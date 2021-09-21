package com.mobile.bnkcl.ui.main.fragment.office

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.bnkc.sourcemodule.base.BaseFragment
import com.bnkc.sourcemodule.dialog.ListChoiceDialog
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.request.findoffice.BranchRequest
import com.mobile.bnkcl.data.response.office.AreaDataResponse
import com.mobile.bnkcl.databinding.FragmentFindOfficeBinding
import com.mobile.bnkcl.ui.alarm.AlarmActivity
import com.mobile.bnkcl.ui.main.adapter.FindOfficeRecyclerAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class FindOfficeFragment : BaseFragment<FragmentFindOfficeBinding>() {

    private var objects: ArrayList<AreaDataResponse>? = ArrayList()
    private val viewModel : FindOfficeViewModel by viewModels()
    private var selectedItem = 0;

    @Inject
    lateinit var listChoiceDialog: ListChoiceDialog

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
        val manager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerFindOffice.layoutManager = manager

        binding.tvAreas.setOnClickListener(View.OnClickListener {
            if (objects != null && objects!!.size > 0) {

                listChoiceDialog = ListChoiceDialog.newInstance(
                    R.drawable.ic_badge_error,
                    getString(R.string.area),
                    viewModel.setUpAreaName(objects!!),
                    selectedItem
                )

                listChoiceDialog.setOnItemListener = {
                    p : Int ->
                    selectedItem = p
                    binding.tvAreas.text = objects!![p].name
                    viewModel.branchRequest = BranchRequest(objects!![p].id.toString(), 1, 10, "")
                    viewModel.reqBranchList()
                }
                listChoiceDialog.isCancelable = true
                listChoiceDialog.show(requireActivity().supportFragmentManager, listChoiceDialog.tag)

            }
        })
        showLoading()
        viewModel.reqAreasList()
        getAreas()
        getBranches()
    }

    fun getAreas(){
        viewModel.areaLiveData.observe(requireActivity()) {
            objects!!.clear()
            val areaRespondObj = AreaDataResponse()
            areaRespondObj.name = getString(R.string.all)
            areaRespondObj.id = 0
            objects!!.add(areaRespondObj)
            objects!!.addAll(it)
            viewModel.branchRequest = BranchRequest("", 1, 10, "")
            viewModel.reqBranchList()
        }
    }

    fun getBranches(){
        viewModel.branchLiveData.observe(requireActivity(),{
            val adapter = FindOfficeRecyclerAdapter(requireActivity().supportFragmentManager)
            adapter.addItemList(it)
            binding.recyclerFindOffice.adapter = adapter
        })
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_find_office
    }
}