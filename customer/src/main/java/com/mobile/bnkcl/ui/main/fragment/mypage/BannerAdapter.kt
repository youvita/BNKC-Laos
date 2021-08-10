package com.mobile.bnkcl.ui.main.fragment.mypage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import com.mobile.bnkcl.R
import com.mobile.bnkcl.utilities.CustomPager
import java.util.*

class BannerAdapter(private val context: Context, private val imageList: ArrayList<Int>) :
    PagerAdapter() {
    private var mCurrentPosition = -1
    override fun getCount(): Int {
        return imageList.size
    }

    override fun isViewFromObject(
        view: View,
        `object` : Any
    ): Boolean {
        return view === `object` as LinearLayout
    }

    override fun setPrimaryItem(
        container: ViewGroup,
        position: Int,
        `object`: Any
    ) {
        super.setPrimaryItem(container, position, `object`)
        if (position != mCurrentPosition) {
            val linearLayout = `object` as LinearLayout
            val pager: CustomPager = container as CustomPager
            if (linearLayout != null) {
                mCurrentPosition = position
                pager.measureCurrentView(linearLayout)
            }
        }
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.banner_item_layout, container, false)
        val imageView =
            view.findViewById<ImageView>(R.id.img_banner)
//        UtilsGlide.loadRounded(context, imageList[position], imageView, null)
        //        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(container.getContext(), BannerDetailsActivity.class);
//                container.getContext().startActivity(intent);
//            }
//        });
        container.addView(view)
        return view
    }

    override fun destroyItem(
        container: ViewGroup,
        position: Int,
        `object`: Any
    ) {
        val view = `object` as View
        container.removeView(view)
    }

}