package com.mobile.bnkcl.ui.main.fragment.service

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bnkc.sourcemodule.base.BaseFragment
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.FragmentLoanServiceBinding

class ServiceFragment : BaseFragment<FragmentLoanServiceBinding>(){

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
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_loan_service
    }

}