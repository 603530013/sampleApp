package com.mobiledrivetech.external.common.component.extensions

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.TextAppearanceSpan
import androidx.annotation.ColorInt
import androidx.core.util.PatternsCompat
import androidx.core.util.PatternsCompat.EMAIL_ADDRESS
import com.mobiledrivetech.external.common.component.tools.BaseLineAlignSpan

typealias TitleComposed = Pair<String, String>
typealias IndexComposed = Pair<Int, Int>

const val EMPTY_VALUE_DASH = "–"

infix fun TitleComposed.withColor(@ColorInt color: Int): CharSequence = when (second.isBlank()) {
    true -> first
    else -> SpannableStringBuilder(first).append(second).apply {
        val index = first.length

        setSpan(
            ForegroundColorSpan(color),
            index,
            length,
            Spanned.SPAN_INCLUSIVE_INCLUSIVE
        )
    }
}

// Email Validation
fun String.isValidEmail(): Boolean = this.isNotEmpty() && EMAIL_ADDRESS.matcher(this).matches()

// Phone number format
// implementation 'io.michaelrocks:libphonenumber-android:8.12.18'
// fun String.formatPhoneNumber(context: Context, region: String): String? {
//    val phoneNumberKit = PhoneNumberUtil.createInstance(context)
//    val number = phoneNumberKit.parse(this, region)
//    if (!phoneNumberKit.isValidNumber(number))
//        return null
//
//    return phoneNumberKit.format(number, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)
// }

fun String.isValidPhoneNumber(): Boolean =
    this.isNotBlank() && "^\\+(?:[0-9] ?){4,14}[0-9]$".toRegex().matches(this)

fun String.isValidSmsCode(): Boolean =
    this.isNotBlank() && "^[0-9]{4,10}\$".toRegex().matches(this)


fun String.isIdenticalPins(confirmation: String): Boolean = this == confirmation

fun IndexComposed.withBaseLine(
    shiftPercentage: Float,
    label: String
): CharSequence = SpannableString(label).apply {
    setSpan(
        BaseLineAlignSpan(shiftPercentage),
        first,
        second,
        Spanned.SPAN_INCLUSIVE_INCLUSIVE
    )
}

fun IndexComposed.withSize(
    dimen: Int,
    label: String
): CharSequence = SpannableString(label).apply {
    setSpan(
        AbsoluteSizeSpan(dimen, true),
        first,
        second,
        Spanned.SPAN_INCLUSIVE_INCLUSIVE
    )
}

fun IndexComposed.withTextAppearence(
    context: Context,
    textAppearance: Int,
    label: CharSequence
): CharSequence = SpannableString(label).apply {
    val appearance = TextAppearanceSpan(context, textAppearance)
    setSpan(
        appearance,
        first,
        second,
        Spanned.SPAN_INCLUSIVE_INCLUSIVE
    )
}

fun IndexComposed.withBold(
    label: CharSequence
): CharSequence = SpannableString(label).apply {
    setSpan(
        StyleSpan(Typeface.BOLD),
        first,
        second,
        Spannable.SPAN_EXCLUSIVE_INCLUSIVE
    )
}

fun String.isValidExceptOf(expectParam: String) = this.isNotBlank() && !this.equals(expectParam, true)

fun String.isValidUri() = PatternsCompat.WEB_URL.matcher(this).matches()
