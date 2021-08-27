package com.mobile.bnkcl.ui.main.adapter

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bnkc.sourcemodule.dialog.ConfirmDialog
import com.mobile.bnkcl.R
import com.bnkc.sourcemodule.ui.ShadowLayout
import com.google.android.gms.dynamic.SupportFragmentWrapper
import com.mobile.bnkcl.data.findoffice.BranchResData
import com.mobile.bnkcl.ui.map.MapActivity
import com.mobile.bnkcl.utilities.FormatUtil
import com.mobile.bnkcl.utilities.UtilsSize
import java.util.*
import javax.inject.Inject

class FindOfficeRecyclerAdapter(private val offices: List<BranchResData>, private val supportFragmentWrapper: FragmentManager) :
    RecyclerView.Adapter<FindOfficeRecyclerAdapter.FindOfficeViewHolder>() {
    private var context: Context? = null

    @Inject
    lateinit var confirmDialog: ConfirmDialog

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FindOfficeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        context = parent.context
        val view: View = inflater.inflate(R.layout.find_office_holder, parent, false)
        return FindOfficeViewHolder(view)
    }

    override fun onBindViewHolder(holder: FindOfficeViewHolder, position: Int) {
        Log.d(">>>>>>>>", "HHH ${offices.size}")
        val office: BranchResData = offices[position]
        holder.tvOfficeName.text = office.name!!.toUpperCase()
        holder.tvDescription.text = Html.fromHtml(office.address)
        if (office.email != null && office.email!!.isNotEmpty()) {
            holder.tvOfficeEmail.text = office.email
        } else {
            holder.tvOfficeEmail.visibility = View.GONE
        }
//        holder.tvTel.setText(
//            FormatUtil.getSeparateFont(
//                context,
//                0,
//                FormatUtil.getTelFormat(office.getTel(), 0).length(),
//                "en",
//                FormatUtil.getTelFormat(office.getTel(), 0)
//            )
//        )
        if (position == 0) {
            if (offices.size == 1) {
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
                holder.slContainer.setLayoutParams(params)
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
                holder.slContainer.setLayoutParams(params)
            }
        } else if (position == offices.size - 1) {
            val params: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 0, 0, UtilsSize.getValueInDP(context!!, 10))
            holder.slContainer.setLayoutParams(params)
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
            holder.slContainer.setLayoutParams(params)
        }
    }

    override fun getItemCount(): Int {
        return offices.size
    }

    private fun sendEmail(sendTo: String) {
        context!!.startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$sendTo")))
    }

    inner class FindOfficeViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var llContact: LinearLayout = itemView.findViewById(R.id.ll_contact)
        var llViewMap: LinearLayout = itemView.findViewById(R.id.ll_view_map)
        var llContainer: LinearLayout = itemView.findViewById(R.id.ll_container)
        var tvOfficeName: TextView = itemView.findViewById(R.id.tv_head_office)
        var tvDescription: TextView = itemView.findViewById(R.id.tv_description)
        var tvTel: TextView = itemView.findViewById(R.id.tv_contact)
        var tvOfficeEmail: TextView = itemView.findViewById(R.id.tv_office_email)
        var slContainer: ShadowLayout = itemView.findViewById(R.id.sl_container)

        init {
            tvOfficeEmail.setOnClickListener { sendEmail(offices[adapterPosition].email!!) }
            llViewMap.setOnClickListener { //set branch info to extra
                val intent = Intent(context, MapActivity::class.java)
                intent.putExtra("branch_id", offices[adapterPosition].id)
                context?.startActivity(intent)
            }
            llContact.setOnClickListener {
                confirmDialog = ConfirmDialog.newInstance(
                    R.drawable.ic_badge_call_now,
                    context!!.getString(R.string.call_now),
                    FormatUtil.getTelFormat(offices[adapterPosition].tel!!, 2)!!,
                    context!!.getString(R.string.call)
                )
                confirmDialog.onConfirmClickedListener {
                    try {
                        val number = Uri.parse(
                            "tel:" + FormatUtil.getTelFormat(
                                offices[adapterPosition].tel!!, 1
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
                confirmDialog.show(supportFragmentWrapper, confirmDialog.tag)

//                DlgAlert.DlgAlertTwoButton(
//                    context,
//                    context!!.getString(R.string.call_now),
//                    R.drawable.badge_call_now,
//                    FormatUtil.getTelFormat(
//                        offices[adapterPosition].getTel(), 2
//                    ),
//                    context!!.getString(R.string.comm_cancel),
//                    context!!.getString(R.string.call),
//                    object : OnDialogListener() {
//                        fun onClickDlgButton(dialogIndex: Int, buttonType: DlgAlert.DIALOG_BTN) {
//                            if (buttonType === DlgAlert.DIALOG_BTN.LEFT_BTN) {
//                            } else {
//                                try {
//                                    val number = Uri.parse(
//                                        "tel:" + FormatUtil.getTelFormat(
//                                            offices[adapterPosition].getTel(), 1
//                                        )
//                                    )
//                                    val callIntent = Intent(Intent.ACTION_DIAL, number)
//                                    context!!.startActivity(callIntent)
//                                } catch (e: ActivityNotFoundException) {
//                                    Toast.makeText(
//                                        context!!.applicationContext,
//                                        "Error in your phone call" + e.message,
//                                        Toast.LENGTH_LONG
//                                    ).show()
//                                }
//                            }
//                        }
//                    },
//                    true
//                )
            }
        }
    }

}