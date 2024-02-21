package com.mobiledrivetech.external.common.component.tools

import android.text.TextPaint
import android.text.style.SuperscriptSpan
import androidx.annotation.FloatRange

class BaseLineAlignSpan(
    @FloatRange(from = 0.0, to = 1.0, fromInclusive = false, toInclusive = false)
    val shiftPercentage: Float
) : SuperscriptSpan() {

    // divide superscript by this number
    private var fontScale = 2

    override fun updateDrawState(tp: TextPaint) {
        // original ascent
        val ascent = tp.ascent()

        // scale down the font
        tp.textSize = tp.textSize / fontScale

        // get the new font ascent
        val newAscent = tp.fontMetrics.ascent

        // move baseline to top of old font, then move down size of new font
        tp.baselineShift += (
            ascent - ascent * shiftPercentage -
                (newAscent - newAscent * shiftPercentage)
            ).toInt()
    }

    override fun updateMeasureState(tp: TextPaint) {
        updateDrawState(tp)
    }
}
