package com.mobile.bnkcl.ui.main.fragment.service

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bnkc.sourcemodule.base.BaseFragment
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.FragmentLoanServiceBinding
import com.mobile.bnkcl.ui.alarm.AlarmActivity
import com.mobile.bnkcl.ui.lease.service.LeaseServiceActivity

class ServiceFragment : BaseFragment<FragmentLoanServiceBinding>(){

    val viewModel: ServiceViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_loan_service, (container), false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoanServiceBinding.bind(view)
        binding.serviceViewModel = viewModel
        observeViewModel()
        initView()
    }

    private fun initView(){
        binding.ivNotify.setOnClickListener {
            startActivity(Intent(activity, AlarmActivity::class.java))
        }
        binding.vbDetail.setOnClickListener {
            startActivity(Intent(requireActivity(), LeaseServiceActivity::class.java))
        }
    }

    private fun observeViewModel() {
        viewModel.actLiveData.observe(viewLifecycleOwner, {
            when(it){
                "DETAIL" -> {
                    startActivity(Intent(requireActivity(), LeaseServiceActivity::class.java))
                }else -> {

                }
            }
        })
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_loan_service
    }

}