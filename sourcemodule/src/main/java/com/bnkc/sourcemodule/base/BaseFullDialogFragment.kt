package com.bnkc.sourcemodule.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.bnkc.library.base.BaseBasicDialogFragment
import com.bnkc.library.base.BaseBasicFullDialogFragment

abstract class BaseFullDialogFragment<T: ViewDataBinding>: BaseBasicFullDialogFragment() {

    lateinit var binding: T

    private var inflatedView: View? = null

    private var dismissListener: (() -> Unit)? = null

    @LayoutRes
    abstract fun getLayoutId(): Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (inflatedView != null) return inflatedView
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        inflatedView = binding.root
        return inflatedView
    }

    override fun onDestroy() {
        super.onDestroy()
        inflatedView = null
    }
}