package com.mobile.bnkcl.util

import android.animation.Animator
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import android.graphics.drawable.AnimatedVectorDrawable
import android.animation.ValueAnimator
import android.content.Context
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.Transformation
import android.widget.ImageView
import com.mobile.bnkcl.R

class UtilAnimation {
    private val avdCompat: AnimatedVectorDrawableCompat? = null
    private val avd: AnimatedVectorDrawable? = null

    companion object {
        /**
         * use to expand myCard
         * @param v is view that you want to expand
         * @param duration millisecond
         */
        fun expand(v: View, height: Int, duration: Int) {
            val mAnimator = slideAnimator(v, 0, height)
            mAnimator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                    v.visibility = View.VISIBLE
                }

                override fun onAnimationEnd(animation: Animator) {}
                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })
            mAnimator.duration = duration.toLong()
            mAnimator.start()
        }

        fun slideAnimator(v: View, start: Int, end: Int): ValueAnimator {
            val animator = ValueAnimator.ofInt(start, end)
            animator.addUpdateListener { valueAnimator -> //Update Height
                val value = valueAnimator.animatedValue as Int
                val layoutParams = v.layoutParams
                layoutParams.height = value
                v.layoutParams = layoutParams
            }
            return animator
        }

        /**
         * use to collapse myCard
         * @param v is view that you want to collapse
         * @param duration millisecond
         */
        fun collapse(v: View, height: Int, duration: Int) {
            val mAnimator = slideAnimator(v, height, 0)
            mAnimator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationEnd(animator: Animator) {
                    //Height=0, but it set visibility to GONE
                    v.visibility = View.GONE
                }

                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })
            mAnimator.start()
        }

        /**
         * use to expand myCard
         * @param v is view that you want to expand
         * @param duration millisecond
         */
        fun expand(v: View, duration: Int) {
            val matchParentMeasureSpec =
                View.MeasureSpec.makeMeasureSpec((v.parent as View).width, View.MeasureSpec.EXACTLY)
            val wrapContentMeasureSpec =
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            v.measure(matchParentMeasureSpec, wrapContentMeasureSpec)
            val targetHeight = v.measuredHeight
            v.layoutParams.height = 0
            v.visibility = View.VISIBLE
            val a: Animation = object : Animation() {
                override fun applyTransformation(
                    interpolatedTime: Float,
                    t: Transformation
                ) {
                    v.layoutParams.height =
                        if (interpolatedTime == 1f) ViewGroup.LayoutParams.WRAP_CONTENT else (targetHeight * interpolatedTime).toInt()
                    v.requestLayout()
                }

                override fun willChangeBounds(): Boolean {
                    return true
                }
            }
            a.duration = duration.toLong()
            v.startAnimation(a)
        }

        /**
         * use to collapse myCard
         * @param v is view that you want to collapse
         * @param duration millisecond
         */
        fun collapse(v: View, duration: Int) {
            val initialHeight = v.measuredHeight
            val a: Animation = object : Animation() {
                override fun applyTransformation(
                    interpolatedTime: Float,
                    t: Transformation
                ) {
                    if (interpolatedTime == 1f) {
                        v.visibility = View.GONE
                    } else {
                        v.layoutParams.height = (initialHeight
                                - (initialHeight * interpolatedTime).toInt())
                        v.requestLayout()
                    }
                }

                override fun willChangeBounds(): Boolean {
                    return true
                }
            }
            a.duration = (v.layoutParams.height + duration).toLong()
            v.startAnimation(a)
        }

        /**
         * Expand or Collpase Animation
         * @param v view to expand or clollpase
         * @param duration time duration
         * @param isExpand true -> Collapse, false -> Expand
         */
        fun expandCollpaseAnimation(v: View, duration: Int, isExpand: Boolean) {
            if (isExpand) {
                collapse(v, duration)
            } else {
                expand(v, duration)
            }
        }

        fun processExpandAnimate(view: View?) {
            val avdCompat: AnimatedVectorDrawableCompat
            val avd: AnimatedVectorDrawable
            if (view is ImageView) {
                val drawable = view.drawable
                if (drawable is AnimatedVectorDrawableCompat) {
                    avdCompat = drawable
                    avdCompat.start()
                } else if (drawable is AnimatedVectorDrawable) {
                    avd = drawable
                    avd.start()
                }
            }
        }

        fun slideFromTopAnimate(context: Context?, view: View) {
            val animation: Animation
            animation = AnimationUtils.loadAnimation(
                context,
                R.anim.slide_top
            )
            view.animation = animation
        }
    }
}