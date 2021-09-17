package com.mobile.bnkcl.ui.main.adapter

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bnkc.sourcemodule.base.BaseAdapter
import com.bnkc.sourcemodule.dialog.ConfirmDialog
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.findoffice.BranchResData
import com.mobile.bnkcl.databinding.FindOfficeHolderBinding
import com.mobile.bnkcl.ui.map.MapActivity
import com.mobile.bnkcl.utilities.FormatUtil
import com.mobile.bnkcl.utilities.UtilsSize
import javax.inject.Inject

class FindOfficeRecyclerAdapter(var supportFragmentManager: FragmentManager) : BaseAdapter<FindOfficeHolderBinding, BranchResData, FindOfficeRecyclerAdapter.ViewHolder>() {
    private var context: Context? = null

    @Inject
    lateinit var confirmDialog: ConfirmDialog

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.find_office_holder
    }

    private fun sendEmail(sendTo: String) {
        context!!.startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$sendTo")))
    }

    class ViewHolder(val binding: FindOfficeHolderBinding) : RecyclerView.ViewHolder(binding.root){
    }

    override fun setViewHolder(parent: ViewGroup): ViewHolder {
        context = parent.context
        return ViewHolder(binding)
    }

    override fun setBindData(holder: ViewHolder, data: BranchResData, position: Int) {
        binding.item = data

        if (position == 0) {
            if (itemCount == 1) {
                val params: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(
                    0,
                    UtilsSize.getValueInDP(context!!, 35),
                    0,
                    UtilsSize.getValueInDP(context!!, 30)
                )
                holder.binding.slContainer.layoutParams = params
            } else {
                val params  = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(
                    0,
                    UtilsSize.getValueInDP(context!!, 35),
                    0,
                    UtilsSize.getValueInDP(context!!, -10)
                )
                holder.binding.slContainer.layoutParams = params
            }
        } else if (position == itemCount - 1) {
            val params: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 0, 0, UtilsSize.getValueInDP(context!!, 10))
            holder.binding.slContainer.layoutParams = params
        } else {
            val params: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(
                0,
                UtilsSize.getValueInDP(context!!, 0),
                0,
                UtilsSize.getValueInDP(context!!, -10)
            )
            holder.binding.slContainer.layoutParams = params
        }

        holder.binding.tvOfficeEmail.setOnClickListener {
            sendEmail(data.email!!)
        }
        holder.binding.llViewMap.setOnClickListener { //set branch info to extra
            val intent = Intent(context, MapActivity::class.java)
            intent.putExtra("branch_id", data.branch_id)
            context?.startActivity(intent)
        }
        holder.binding.llContact.setOnClickListener {
            confirmDialog = ConfirmDialog.newInstance(
                R.drawable.ic_badge_call_now,
                context!!.getString(R.string.call_now),
                FormatUtil.getTelFormat(data.tel!!, 2)!!,
                context!!.getString(R.string.call)
            )
            confirmDialog.onConfirmClickedListener {
                try {
                    val number = Uri.parse(
                        "tel:" + FormatUtil.getTelFormat(
                            data.tel!!, 1
                        )
                    )
                    val callIntent = Intent(Intent.ACTION_DIAL, number)
                    context!!.startActivity(callIntent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(
                        context!!.applicationContext,
                        "Error in your phone call" + e.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            confirmDialog.isCancelable = false
            confirmDialog.show(supportFragmentManager, confirmDialog.tag)

        }
    }

}