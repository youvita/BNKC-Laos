package com.mobile.bnkcl.ui.main.fragment.menu

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.view.menu.MenuView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bnkc.sourcemodule.base.BaseFragment
import com.mobile.bnkcl.R
import com.mobile.bnkcl.databinding.FragmentMenuBinding
import com.mobile.bnkcl.ui.login.LoginActivity
import com.mobile.bnkcl.ui.main.MainViewModel
import com.mobile.bnkcl.ui.otp.OtpActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MenuFragment : BaseFragment<FragmentMenuBinding>(){

    private val viewModel: MenuViewModel by viewModels()

    override fun getLayoutId(): Int {
        return R.layout.fragment_menu
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.context = requireContext()
        binding.menuViewModel = viewModel

//        binding.btnLogin.setOnClickListener {
//            val intent = Intent(context, OtpActivity::class.java)
//            startActivity(intent)
//        }

    }

}