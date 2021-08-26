package com.bnkc.sourcemodule.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.AdapterView.OnItemClickListener
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

    private val mlistener: onItemClickLisenter? = null
    private var confirmClickListener: (() -> Unit)? = null

    companion object {
        private const val ICON = "icon"
        private const val TITLE = "title"
        private const val OBJ = "obj"
        private const val SELECTED_ITEM = "selected_item"
        private const val item = 4

        var data : ArrayList<Any> = ArrayList()

        @Synchronized
        fun newInstance(icon: Int, title: String, obj: ArrayList<Any>, selectedItem : Int): ListChoiceDialog {
            data = obj
            return ListChoiceDialog().apply {
                arguments = Bundle().apply {
                    putInt(ICON, icon)
                    putString(TITLE, title)
                    putInt(SELECTED_ITEM, selectedItem)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var selectedItem = arguments!!.getInt(ListChoiceDialog.SELECTED_ITEM)
        binding.imgTitle.background = view.context.getDrawable(arguments?.getInt(
            ListChoiceDialog.ICON
        )!!)
        binding.alertTitle.text = arguments?.getString(ListChoiceDialog.TITLE)

        val sortList = ArrayList<Any>()
        var obj = data
        val map: MutableMap<String, Any> =
            LinkedHashMap()
         if (obj.get(0) is String) {
            for (i in obj.indices) {
                sortList.add(obj.get(i))
            }
        }

        val adapter =
            ListChoiceAdapter(context!!.applicationContext, sortList, selectedItem)

        val handler = Handler()
        handler.postDelayed({
            try {
                val h1 = binding.lvLoanTerm.height
                val itemHeight: Int
                if (item % 2 == 0) {
                    itemHeight =
                        h1 / item // find item height
                } else {
                    itemHeight =
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

        // set on item selected
        binding.lvLoanTerm.onItemClickListener =
            OnItemClickListener { parent, view, position, id ->
                if (obj.get(0) is Map<*, *>) mlistener!!.onClickItem(
                    position,
                    map
                ) else mlistener!!.onClickItem(
                    position,
                    sortList[position]
                )
            }

    }

    interface onItemClickLisenter {
        fun onClickItem(dialogIndex: Int, obj: Any?)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        confirmClickListener = null
    }

    fun onConfirmClickedListener(confirmListener: (() -> Unit)) =
        apply { this.confirmClickListener = confirmClickListener }

}