package com.mobile.bnkcl.ui.main.adapter

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobile.bnkcl.R
import com.mobile.bnkcl.com.view.ShadowLayout
import com.mobile.bnkcl.data.findoffice.BranchResData
import com.mobile.bnkcl.ui.map.MapActivity
import com.mobile.bnkcl.utilities.UtilsSize
import java.util.*

class FindOfficeRecyclerAdapter(offices: ArrayList<BranchResData>) :
    RecyclerView.Adapter<FindOfficeRecyclerAdapter.FindOfficeViewHolder>() {
    private var context: Context? = null
    private val offices: ArrayList<BranchResData> = offices


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FindOfficeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        context = parent.context
        val view: View = inflater.inflate(R.layout.find_office_holder, parent, false)
        return FindOfficeViewHolder(view)
    }

    override fun onBindViewHolder(holder: FindOfficeViewHolder, position: Int) {
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
        var llContact: LinearLayout
        var llViewMap: LinearLayout
        var llContainer: LinearLayout
        var tvOfficeName: TextView
        var tvDescription: TextView
        var tvTel: TextView
        var tvOfficeEmail: TextView
        var slContainer: ShadowLayout

        init {
            slContainer = itemView.findViewById(R.id.sl_container)
            llContainer = itemView.findViewById(R.id.ll_container)
            tvOfficeName = itemView.findViewById(R.id.tv_head_office)
            tvDescription = itemView.findViewById(R.id.tv_description)
            tvTel = itemView.findViewById(R.id.tv_contact)
            tvOfficeEmail = itemView.findViewById(R.id.tv_office_email)
            llContact = itemView.findViewById(R.id.ll_contact)
            llViewMap = itemView.findViewById(R.id.ll_view_map)
            tvOfficeEmail.setOnClickListener { sendEmail(offices[adapterPosition].email!!) }
            llViewMap.setOnClickListener { //set branch info to extra
//                val extrasBranch = ExtrasBranch(context)
//                extrasBranch.Param.setId(java.lang.String.valueOf(offices[adapterPosition].id))
//                extrasBranch.Param.setAddress(offices[adapterPosition].address)
//                extrasBranch.Param.setName(offices[adapterPosition].name)
//                extrasBranch.Param.setTel(offices[adapterPosition].tel)
//                extrasBranch.Param.setLat(
//                    java.lang.String.valueOf(
//                        offices[adapterPosition].location!!.lat
//                    )
//                )
//                extrasBranch.Param.setLon(
//                    java.lang.String.valueOf(
//                        offices[adapterPosition].location!!.lon
//                    )
//                )
//                extrasBranch.Param.setZoomLevel(
//                    java.lang.String.valueOf(
//                        offices[adapterPosition].lo
//                    )
//                )
                val intent = Intent(context, MapActivity::class.java)
//                intent.putExtras(extrasBranch.getBundle())
                context!!.startActivity(intent)
            }
            llContact.setOnClickListener {
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