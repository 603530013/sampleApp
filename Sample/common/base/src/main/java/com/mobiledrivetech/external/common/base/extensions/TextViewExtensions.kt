package com.mobiledrivetech.external.common.base.extensions

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView

/**
 * Add an action which will be invoked before the text changed.
 *
 * @return the [TextWatcher] added to the TextView
 */
inline fun TextView.beforeTextChanged(
    crossinline action: (
        text: CharSequence?,
        start: Int,
        count: Int,
        after: Int
    ) -> Unit
) = setTextChangedListener(beforeTextChanged = action)

/**
 * Add an action which will be invoked when the text is changing.
 *
 * @return the [TextWatcher] added to the TextView
 */
inline fun TextView.onTextChanged(
    crossinline action: (
        text: CharSequence?,
        start: Int,
        before: Int,
        count: Int
    ) -> Unit
) = setTextChangedListener(onTextChanged = action)

/**
 * Add an action which will be invoked after the text changed.
 *
 * @return the [TextWatcher] added to the TextView
 */
inline fun TextView.afterTextChanged(
    crossinline action: (text: Editable?) -> Unit
) = setTextChangedListener(afterTextChanged = action)

/**
 * Add a text changed listener to this TextView using the provided actions
 *
 * @return the [TextWatcher] added to the TextView
 */
inline fun TextView.setTextChangedListener(
    crossinline beforeTextChanged: (
        text: CharSequence?,
        start: Int,
        count: Int,
        after: Int
    ) -> Unit = { _, _, _, _ -> },
    crossinline onTextChanged: (
        text: CharSequence?,
        start: Int,
        before: Int,
        count: Int
    ) -> Unit = { _, _, _, _ -> },
    crossinline afterTextChanged: (text: Editable?) -> Unit = {}
): TextWatcher {
    val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            removeTextChangedListener(this)
            afterTextChanged.invoke(s)
            addTextChangedListener(this)
        }

        override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {
            removeTextChangedListener(this)
            beforeTextChanged.invoke(text, start, count, after)
            addTextChangedListener(this)
        }

        override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
            removeTextChangedListener(this)
            onTextChanged.invoke(text, start, before, count)
            addTextChangedListener(this)
        }
    }
    addTextChangedListener(textWatcher)
    return textWatcher
}
