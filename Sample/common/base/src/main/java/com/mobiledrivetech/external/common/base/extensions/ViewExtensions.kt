package com.mobiledrivetech.external.common.base.extensions

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
private const val PERCENTAGE = 100

fun View.updateVisibility(value: Int) {
    if (visibility != value) {
        visibility = value
    }
}

fun View.hideSoftInput() {
    ContextCompat.getSystemService(context, InputMethodManager::class.java)
        ?.hideSoftInputFromWindow(windowToken, 0)
}

fun View.getVisibleHeightPercentage(rect: Rect): Double {
    val isParentViewEmpty = this.getLocalVisibleRect(rect)
    val visibleHeight = rect.height().toDouble()
    val height = this.measuredHeight
    val viewVisibleHeightPercentage = visibleHeight.div(height).times(PERCENTAGE)
    return when {
        isParentViewEmpty -> viewVisibleHeightPercentage
        else -> 0.0
    }
}

fun View?.getLocationInView(parent: View?): Rect =
    when {
        this == null -> Rect().apply { setEmpty() }
        parent == null -> Rect().apply { setEmpty() }

        else -> {
            val context: Context? = this.context
            val decorView: View? = (context as? Activity)?.window?.decorView
            val parentDecorView = (decorView?.parent as? View) ?: decorView

            val result = Rect()
            val tmpRect = Rect()

            var tmp = this

            if (this === parent) {
                this.getHitRect(result)
            } else {
                while (tmp !== parentDecorView && tmp !== parent) {
                    tmp?.getHitRect(tmpRect)
                    result.left += tmpRect.left
                    result.top += tmpRect.top
                    tmp = tmp?.parent as? View
                }
                result.right = result.left + this.measuredWidth
                result.bottom = result.top + this.measuredHeight
            }
            result
        }
    }
