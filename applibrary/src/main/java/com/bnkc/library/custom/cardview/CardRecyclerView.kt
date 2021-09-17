package com.bnkc.library.custom.cardview

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bnkc.library.util.ScreenUtil
import kotlin.math.abs
import kotlin.math.max

class CardRecyclerView {

   private var mRecyclerView: RecyclerView? = null
   private var mContext: Context? = null

   private var mScale = 0.9f
   private var mPagePadding = 15
   private var mShowLeftCardWidth = 15

   private var mCardWidth: Int = 0
   private var mOnePageWidth: Int = 0
   private var mCardGalleryWidth: Int = 0

   private var currentItemPos: Int = 0
   private var mCurrentItemOffset: Int = 0

   private val pagerSnapHelper = PagerSnapHelper()

   fun attachToRecyclerView(mRecyclerView: RecyclerView?) {
      this.mRecyclerView = mRecyclerView
      mContext = mRecyclerView?.context
      mRecyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
         override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (dx != 0) {
               mCurrentItemOffset += dx
               computeCurrentItemPos()
               onScrolledChangedCallback()
            }
         }
      })

      initWidth()
      mRecyclerView?.onFlingListener = null
      pagerSnapHelper.attachToRecyclerView(mRecyclerView)
   }

   private fun initWidth() {
      mRecyclerView?.post {
         mCardGalleryWidth = mRecyclerView!!.width
         mCardWidth =
            mCardGalleryWidth - ScreenUtil.dip2px(
               mContext!!,
               (2 * (mPagePadding + mShowLeftCardWidth)).toFloat()
            )
         mOnePageWidth = mCardWidth
         mRecyclerView?.smoothScrollToPosition(currentItemPos)
         onScrolledChangedCallback()
      }
   }

   private fun getDestItemOffset(destPos: Int): Int {
      return mOnePageWidth * destPos
   }

   private fun computeCurrentItemPos() {
      if (mOnePageWidth <= 0) return
      var pageChanged = false
      if (abs(mCurrentItemOffset - currentItemPos * mOnePageWidth) >= mOnePageWidth) {
         pageChanged = true
      }
      if (pageChanged) {
         currentItemPos
         currentItemPos = mCurrentItemOffset / mOnePageWidth
      }
   }

   private fun onScrolledChangedCallback() {
      val offset = mCurrentItemOffset - currentItemPos * mOnePageWidth
      val percent = max(abs(offset) * 1.0 / mOnePageWidth, 0.0001).toFloat()

      var leftView: View? = null
      val currentView: View? = mRecyclerView!!.layoutManager!!.findViewByPosition(currentItemPos)
      var rightView: View? = null
      if (currentItemPos > 0) {
         leftView = mRecyclerView!!.layoutManager!!.findViewByPosition(currentItemPos - 1)
      }
      if (currentItemPos < mRecyclerView!!.adapter!!.itemCount - 1) {
         rightView = mRecyclerView!!.layoutManager!!.findViewByPosition(currentItemPos + 1)
      }

      if (leftView != null) {
         // y = (1 - mScale)x + mScale
         leftView.scaleY = (1 - mScale) * percent + mScale
      }
      if (currentView != null) {
         // y = (mScale - 1)x + 1
         currentView.scaleY = (mScale - 1) * percent + 1
      }
      if (rightView != null) {
         // y = (1 - mScale)x + mScale
         rightView.scaleY = (1 - mScale) * percent + mScale
      }
   }

   fun setScale(scale: Float) {
      mScale = scale
   }

   fun setPagePadding(pagePadding: Int) {
      mPagePadding = pagePadding
   }

   fun setShowLeftCardWidth(showLeftCardWidth: Int) {
      mShowLeftCardWidth = showLeftCardWidth
   }
}
