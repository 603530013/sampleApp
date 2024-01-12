package com.mobiledrivetech.external.common.fundamental.extensions

import android.os.Bundle
import androidx.annotation.VisibleForTesting
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.mobiledrivetech.external.common.fundamental.R
import com.mobiledrivetech.external.common.fundamental.extensions.NavigationType.FromBottomToTop
import com.mobiledrivetech.external.common.fundamental.extensions.NavigationType.FromLeftToRight
import com.mobiledrivetech.external.common.fundamental.extensions.NavigationType.FromRightToLeft
import com.mobiledrivetech.external.common.fundamental.extensions.NavigationType.FromTopToBottom
import com.mobiledrivetech.external.common.fundamental.extensions.NavigationType.PopUpAlpha

const val PIN_LABEL = "pinLabel"
fun NavController.navigate(
    navigationType: NavigationType,
    id: Int,
    args: Bundle? = null,
    navOptions: NavOptions.Builder.() -> NavOptions.Builder? = { this }
) {
    when (navigationType) {
        FromBottomToTop -> navigateSlideVertical(id, args, navOptions)
        FromTopToBottom -> navigateSlideVerticalReverse(id, args, navOptions)
        FromLeftToRight -> navigateSlideHorizontal(id, args, navOptions)
        FromRightToLeft -> navigateSlideHorizontalReverse(id, args, navOptions)
        PopUpAlpha -> navigatePopUp(id, args, navOptions)
        else -> IllegalArgumentException("navigationType $navigationType not supported")
    }
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
internal fun NavController.navigateSlideHorizontal(
    id: Int,
    args: Bundle? = null,
    navOptions: NavOptions.Builder.() -> NavOptions.Builder? = { this }
) {
    val nav = NavOptions.Builder()
        .setEnterAnim(R.anim.from_right)
        .setExitAnim(R.anim.to_left)
        .setPopEnterAnim(R.anim.from_left)
        .setPopExitAnim(R.anim.to_right)

    navigate(id, args, navOptions(nav)?.build())
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
internal fun NavController.navigateSlideHorizontalReverse(
    id: Int,
    args: Bundle? = null,
    navOptions: NavOptions.Builder.() -> NavOptions.Builder? = { this }
) {
    val nav = NavOptions.Builder()
        .setEnterAnim(R.anim.to_right)
        .setExitAnim(R.anim.from_left)
        .setPopEnterAnim(R.anim.to_left)
        .setPopExitAnim(R.anim.from_right)

    navigate(id, args, navOptions(nav)?.build())
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
internal fun NavController.navigateSlideVertical(
    id: Int,
    args: Bundle? = null,
    navOptions: NavOptions.Builder.() -> NavOptions.Builder? = { this }
) {
    val nav = NavOptions.Builder()
        .setEnterAnim(R.anim.from_bottom)
        .setPopExitAnim(R.anim.to_bottom)
        .setExitAnim(R.anim.empty)
        .setPopEnterAnim(R.anim.empty)

    val currentLabel = currentDestination?.label
    val label = args?.getString(PIN_LABEL)

    if (currentLabel != label) {
        navigate(id, args, navOptions(nav)?.build())
    }
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
internal fun NavController.navigatePopUp(
    id: Int,
    args: Bundle? = null,
    navOptions: NavOptions.Builder.() -> NavOptions.Builder? = { this }
) {
    val nav = NavOptions.Builder()
        .setEnterAnim(R.anim.from_alpha)
        .setPopExitAnim(R.anim.to_alpha)
        .setExitAnim(R.anim.empty)
        .setPopEnterAnim(R.anim.empty)

    navigate(id, args, navOptions(nav)?.build())
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
internal fun NavController.navigateSlideVerticalReverse(
    id: Int,
    args: Bundle? = null,
    navOptions: NavOptions.Builder.() -> NavOptions.Builder? = { this }
) {
    val nav = NavOptions.Builder()
        .setEnterAnim(R.anim.from_top)
        .setPopExitAnim(R.anim.to_top)
        .setExitAnim(R.anim.empty)
        .setPopEnterAnim(R.anim.empty)

    val currentLabel = currentDestination?.label
    val label = args?.getString(PIN_LABEL)

    if (currentLabel != label) {
        navigate(id, args, navOptions(nav)?.build())
    }
}

sealed class NavigationType {
    object FromBottomToTop : NavigationType()
    object FromTopToBottom : NavigationType()
    object FromLeftToRight : NavigationType()
    object FromRightToLeft : NavigationType()
    object PopUpAlpha : NavigationType()
}
