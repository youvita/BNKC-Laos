package com.mobile.bnkcl.ui.adapter.cscenter

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bnkc.sourcemodule.base.BaseAdapter
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.response.cscenter.ClaimDataResponse
import com.mobile.bnkcl.data.response.cscenter.ClaimItems
import com.mobile.bnkcl.databinding.ItemCsQuestionBinding
import com.mobile.bnkcl.ui.cscenter.AskBNKCDetailActivity

class AskQuestionAdapter(var context: Context):
     BaseAdapter<ItemCsQuestionBinding, ClaimItems, AskQuestionAdapter.ViewHolder>() {
    private var mList : ArrayList<ClaimItems> = ArrayList()


    class ViewHolder(val binding: ItemCsQuestionBinding) : RecyclerView.ViewHolder(binding.root){
         fun setBinding(items: ClaimItems, position: Int){
             binding to items
             binding.executePendingBindings()
         }
     }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_cs_question
    }

    override fun setViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(binding)
    }

    override fun setBindData(holder: ViewHolder, data: ClaimItems, position: Int) {
         binding.claimItems = data
        var items : ClaimItems = mList[position]

        if (null == items.replied_on || "".equals(items.replied_on, ignoreCase = true)) {

            holder.binding.tvAnswer.text = context.getString(R.string.cs_13)
            holder.binding.tvAnswer.background = context.getDrawable(R.drawable.round_solid_ffeeee_18)
            holder.binding.tvAnswer.setTextColor(context.resources.getColor(R.color.color_d7191f))
        } else {
            holder.binding.tvAnswer.text = context.getString(R.string.cs_12)
            holder.binding.tvAnswer.background = context.getDrawable(R.drawable.round_solid_e0f2f1_18)
            holder.binding.tvAnswer.setTextColor(context.resources.getColor(R.color.color_00695c))
        }
        if (null != items.category) {
            when {
                items.category.equals("1") -> holder.binding.tvCategory.text = context.getString(
                    R.string.cs_04
                ) + " / "
                items.category
                    .equals("2") -> holder.binding.tvCategory.text = context.getString(R.string.cs_05) + " / "
                items.category
                    .equals("3") -> holder.binding.tvCategory.text = context.getString(R.string.cs_17) + " / "
            }
        }

        holder.binding.llWrapQuestion.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, AskBNKCDetailActivity::class.java)
            intent.putExtra("CLAIM_ID", items.id)
            intent.putExtra("CATEGORY", items.category)
            intent.putExtra("CATEGORY_NAME", items.category_name)
            context.startActivity(intent)
        })
        holder.setBinding(items, position)

    }

}