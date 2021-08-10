package com.mobile.bnkcl.ui.main.fragment.menu

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
import com.mobile.bnkcl.ui.main.MainViewModel

class MenuFragment : BaseFragment<FragmentMenuBinding>(){

    private val viewModel: MenuViewModel by viewModels()

    override fun getLayoutId(): Int {
        return R.layout.fragment_menu
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            Toast.makeText(context,"Login now success", Toast.LENGTH_LONG).show()
        }

    }

}