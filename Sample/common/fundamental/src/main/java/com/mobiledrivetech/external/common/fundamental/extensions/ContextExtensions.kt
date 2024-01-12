@file:Suppress("TooManyFunctions")

package com.mobiledrivetech.external.common.fundamental.extensions

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.format.DateFormat
import android.util.Log
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.PluralsRes
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.time.format.DateTimeFormatter

private const val TIME_FORMAT_24 = "H:mm"
private const val TIME_FORMAT_12 = "h:mm a"

/**
 * Get resource string from optional id
 *
 * @param resId Resource string identifier.
 * @return The key value if exist, otherwise empty.
 */
infix fun Context.asString(@StringRes resId: Int?) = resId?.let { getString(it) } ?: ""

/**
 * Convert resource name to string identifier if exist
 *
 * @param 0 if the name does not exist
 */
@SuppressLint("DiscouragedApi")
infix fun Context.asStringRes(name: String) = resources.getIdentifier(name, "string", packageName)

@SuppressLint("DiscouragedApi")
infix fun Context.asAttrRes(name: String) = resources.getIdentifier(name, "attr", packageName)

@SuppressLint("DiscouragedApi")
infix fun Context.asDrawableRes(name: String) =
    resources.getIdentifier(name, "drawable", packageName)

/**
 * Get plurals string from id
 *
 * @param resId Resource plurals identifier.
 * @return The key value if exist, otherwise empty.
 */
fun Context.getPlurals(@PluralsRes resId: Int, quantity: Int, quantityLabel: Any = quantity) =
    resources.getQuantityString(resId, quantity, quantityLabel)

/**
 * get resource color from id
 */
@ColorInt
fun Context.getColorFromAttr(
    @AttrRes attrColor: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true
): Int {
    if (!theme.resolveAttribute(attrColor, typedValue, resolveRefs)) {
        Log.w("ContextExtensions", "Failed to resolve attribute: $attrColor")
    }
    return typedValue.data
}

fun Context.getAttribute(
    attrId: Int,
    attrNameId: Int,
    block: (TypedArray) -> Unit
) {
    val typedValue = TypedValue()
    if (!theme.resolveAttribute(attrId, typedValue, true)) {
        Log.w("ContextExtensions", "Failed to resolve attribute: attrId: $attrId | attrNameId: $attrNameId")
    }
    val attributes = intArrayOf(attrNameId)
    val array = obtainStyledAttributes(typedValue.data, attributes)
    block(array)
    array.recycle()
}

/**
 * get resource color from id
 */
@IdRes
fun Context.getIdResourceFromAttr(
    @AttrRes attrRes: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true
): Int {
    if (!theme.resolveAttribute(attrRes, typedValue, resolveRefs)) {
        Log.w("ContextExtensions", "Failed to resolve attribute: $attrRes")
    }
    return typedValue.data
}

fun Context.getTypedArrayFromAttr(
    @AttrRes attrRes: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true
): TypedValue {
    if (!theme.resolveAttribute(attrRes, typedValue, resolveRefs)) {
        Log.w("ContextExtensions", "Failed to resolve attribute: $attrRes")
    }
    return typedValue
}

@DrawableRes
infix fun Context.asDrawableRes(@AttrRes attrRes: Int) = getTypedArrayFromAttr(attrRes).resourceId

infix fun Context.asDrawable(image: Int?): Drawable? {
    if (image == null || image == 0) {
        return null
    }

    return try {
        ContextCompat.getDrawable(this, this asDrawableRes image)
    } catch (ex: Exception) {
        try {
            ContextCompat.getDrawable(this, image)
        } catch (ex1: Exception) {
            null
        }
    }
}

@ColorInt
infix fun Context.asColorRes(@AttrRes attrRes: Int) = getColorFromAttr(attrRes)

private const val HALF = 0.5

fun Fragment.startSafeActivity(intent: Intent, onError: (Exception) -> Unit) {
    try {
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Log.w("ContextExtensions", "$e startSafeActivity")
        onError(e)
    }
}

fun Context.startSafeActivity(intent: Intent, onError: (Exception) -> Unit) {
    try {
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Log.w("ContextExtensions", "$e startSafeActivity")
        onError(e)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun Context.getSystemTimeFormatter(): DateTimeFormatter {
    val pattern = if (DateFormat.is24HourFormat(this)) {
        TIME_FORMAT_24
    } else {
        TIME_FORMAT_12
    }
    return DateTimeFormatter.ofPattern(pattern)
}
