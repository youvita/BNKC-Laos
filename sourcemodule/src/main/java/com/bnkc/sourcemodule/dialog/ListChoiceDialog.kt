package com.bnkc.sourcemodule.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import androidx.core.content.ContextCompat
import com.bnkc.sourcemodule.R
import com.bnkc.sourcemodule.base.BaseDialogFragment
import com.bnkc.sourcemodule.databinding.ListChoiceDialogBinding
import com.bnkc.sourcemodule.dialog.adapter.ListChoiceAdapter
import com.bnkc.sourcemodule.util.setListViewHeightBasedOnChildren
import java.util.*
import kotlin.collections.ArrayList

class ListChoiceDialog : BaseDialogFragment<ListChoiceDialogBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.list_choice_dialog
    }
//    var onConfirmClickListener: ((pos : Int) -> Unit) = { }

    var setOnItemListener : (pos: Int) -> Unit = {}

    companion object {
        private const val ICON = "icon"
        private const val TITLE = "title"
        private const val OBJ = "obj"
        private const val SELECTED_ITEM = "selected_item"
        private const val item = 4

        var data : ArrayList<String> = ArrayList()

        @Synchronized
        fun newInstance(icon: Int, title: String, obj: ArrayList<String>, selectedItem : Int): ListChoiceDialog {
            data = obj
            return ListChoiceDialog().apply {
                arguments = Bundle().apply {
                    putInt(ICON, icon)
                    putString(TITLE, title)
                    putStringArrayList(OBJ, obj)
                    putInt(SELECTED_ITEM, selectedItem)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var selectedItem = requireArguments().getInt(SELECTED_ITEM)
        binding.imgTitle.background = ContextCompat.getDrawable(view.context, arguments?.getInt(
            ICON
        )!!)
        binding.alertTitle.text = arguments?.getString(TITLE)

        val adapter =
            ListChoiceAdapter(requireContext(), data, selectedItem)

        val handler = Handler()
        handler.postDelayed({
            try {
                val h1 = binding.lvLoanTerm.height
                val itemHeight: Int = if (item % 2 == 0) {
                    h1 / item // find item height
                } else {
                    h1 / item / 2 // if number item is odd we have to divide by 2
                }
                binding.lvLoanTerm.smoothScrollToPositionFromTop(selectedItem, h1 / 2 - itemHeight, 0)
            } catch (e: Exception) {
            }
        }, 10)
        binding.lvLoanTerm.adapter = adapter
        binding.lvLoanTerm.itemsCanFocus = false
        //Measure child view to set maximum List height to show child
        //Measure child view to set maximum List height to show child
        binding.lvLoanTerm.setListViewHeightBasedOnChildren(
            binding.lvLoanTerm,
            item
        )
        // set on item selected
        binding.lvLoanTerm.onItemClickListener =
            OnItemClickListener { parent, view, position, id ->
                setOnItemListener(position)
                dismiss()
                Log.d(">>>>>", "List click $position")
            }

    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }

//    fun onConfirmClickedListener(confirmListener: ((pos : Int) -> Unit)) =
//        apply { this.confirmClickListener = confirmListener }

}