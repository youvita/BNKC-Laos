package com.mobile.bnkcl.ui.adapter.cscenter

import android.content.Context
import android.content.Intent
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bnkc.sourcemodule.base.BaseAdapter
import com.mobile.bnkcl.R
import com.mobile.bnkcl.data.response.cscenter.ClaimItems
import com.mobile.bnkcl.databinding.ItemCsQuestionBinding
import com.mobile.bnkcl.ui.cscenter.AskBNKCDetailActivity

class AskQuestionAdapter :
     BaseAdapter<ItemCsQuestionBinding, ClaimItems, AskQuestionAdapter.ViewHolder>() {

     lateinit var context: Context

    class ViewHolder(val binding: ItemCsQuestionBinding) : RecyclerView.ViewHolder(binding.root){
     }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_cs_question
    }

    override fun setViewHolder(parent: ViewGroup): ViewHolder {
        context = parent.context
        return ViewHolder(binding)
    }

    override fun setBindData(holder: ViewHolder, data: ClaimItems, position: Int) {
         binding.item = data

//        holder.binding.tvCreateOn.text = FormatUtils.getDateFormat(data.created_on,6)
//        Log.d(">>>>", "tvCreateOn: "+FormatUtils.getDateFormat(data.created_on,6))
       // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        //}//

        holder.binding.llWrapQuestion.setBackgroundColor(context.resources.getColor(R.color.ask_bnk))

        if (null == data.replied_on || "".equals(data.replied_on, ignoreCase = true)) {
            holder.binding.tvAnswer.text = context.getString(R.string.cs_13)
            holder.binding.tvAnswer.background = context.getDrawable(R.drawable.round_solid_ffeeee_18)
            holder.binding.tvAnswer.setTextColor(context.resources.getColor(R.color.color_d7191f))
        } else {
            holder.binding.tvAnswer.text = context.getString(R.string.cs_12)
            holder.binding.tvAnswer.background = context.getDrawable(R.drawable.round_solid_e0f2f1_18)
            holder.binding.tvAnswer.setTextColor(context.resources.getColor(R.color.color_00695c))
        }

            when {
                data.category?.equals("1") == true -> holder.binding.tvCategory.text = context?.getString(R.string.cs_04) + " / "
                data.category?.equals("2") == true -> holder.binding.tvCategory.text = context?.getString(R.string.cs_05) + " / "
                data.category?.equals("3") == true -> holder.binding.tvCategory.text = context?.getString(R.string.cs_17) + " / "
            }

        holder.binding.llWrapQuestion.setOnClickListener {
            val intent = Intent(context, AskBNKCDetailActivity::class.java)
            intent.putExtra("CLAIM_ID", data.id)
            intent.putExtra("CATEGORY", data.category)
//            intent.putExtra("CATEGORY_NAME", data.category_name)
            context.startActivity(intent)
        }


    }

}