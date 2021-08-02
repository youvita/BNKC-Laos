package com.bnkc.sourcemodule.base

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.bnkc.library.app.getWindowHeight
import com.bnkc.sourcemodule.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetDialog<B: ViewDataBinding>: BottomSheetDialogFragment() {

    /** handle item binding */
    lateinit var binding: B

    /** handle event bottom sheet state */
    var bottomSheetCallback: BottomSheetBehavior.BottomSheetCallback? = null

    /** handle item layout */
    abstract fun getLayoutId(): Int

    /** handle keep status bar color app */
    override fun getTheme() = R.style.BottomSheetDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return (super.onCreateDialog(savedInstanceState) as BottomSheetDialog).apply {
            theme
            requireNotNull(window).attributes.windowAnimations = R.style.BottomSheetDialogSlideDown
            setOnShowListener { setupBottomSheetDialog(this) }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        bottomSheetCallback = null
        super.onDismiss(dialog)
    }

    @Suppress("DEPRECATION")
    open fun setupBottomSheetDialog(dialog: BottomSheetDialog) {
        val bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)
                as FrameLayout?
        val behavior = BottomSheetBehavior.from(bottomSheet!!)
            .apply { setBottomSheetCallback() }
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.setOnShowListener(null)
    }

    /**
     * Event state call back
     * STATE_COLLAPSED : state when the bottom sheet is collapsed.
     * STATE_DRAGGING : state when the bottom sheet is dragging.
     * STATE_EXPANDED : state when the bottom sheet is expanded.
     * STATE_HIDDEN : state when the bottom sheet is hidden.
     * STATE_SETTLING : state when the bottom sheet is settling.
     */
    private fun setBottomSheetCallback() {
        bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                onBottomSheetSlide(bottomSheet, slideOffset)
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {dismiss()}
            }
        }
    }

    open fun onBottomSheetSlide(bottomSheet: View, slideOffset: Float) {}

    /**
     * Setting height bottom sheet dialog to full screen
     */
    fun setupFullHeight(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet = bottomSheetDialog.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from<FrameLayout?>(requireNotNull(bottomSheet))
        val layoutParams = bottomSheet?.layoutParams
        val windowHeight = getWindowHeight()
        layoutParams?.height = windowHeight
        bottomSheet?.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }
}