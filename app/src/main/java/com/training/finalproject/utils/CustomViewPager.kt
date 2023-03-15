package com.training.finalproject.utils

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.Transformation
import androidx.viewpager.widget.ViewPager
import com.training.finalproject.adapter.PagerAdapter

class CustomViewPager : ViewPager {
    private var currentView: View? = null
    private var animStart = false

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var heightMeasureSpec = heightMeasureSpec
        if (!animStart && adapter != null){
            var height =0
            currentView = (adapter as PagerAdapter).getCurrent()
            height = currentView?.measuredHeight ?: 0
            if (height < minimumHeight)
                height = minimumHeight
            val newHeight = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
            if (layoutParams.height != 0 && heightMeasureSpec != newHeight){
                val targetHeight = height
                val currentHeight = layoutParams.height
                val heightChange = targetHeight - currentHeight
                val a =object : Animation(){
                    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                        if (interpolatedTime >= 1){
                            layoutParams.height = targetHeight
                        } else{
                            val stepHeight = (heightChange * interpolatedTime).toInt()
                            layoutParams.height = currentHeight + stepHeight
                        }
                        requestLayout()
                    }

                    override fun willChangeBounds(): Boolean {
                        return true
                    }

                }
                a.setAnimationListener(object : AnimationListener{
                    override fun onAnimationStart(animation: Animation?) {
                        animStart = true
                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        animStart = false
                    }

                    override fun onAnimationRepeat(animation: Animation?) = Unit
                })
                a.duration = 1000
                startAnimation(a)
                animStart = true
            } else{
                heightMeasureSpec = newHeight
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    }

    fun measureCurrentView(currentView: View) {
        this.currentView = currentView
        requestLayout()
    }
}