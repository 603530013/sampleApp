package com.mobiledrivetech.external.common.fundamental.routes

import android.os.Bundle
import androidx.annotation.Keep
import androidx.annotation.StringRes
import com.mobiledrivetech.external.common.fundamental.R
import com.mobiledrivetech.external.common.fundamental.coordinator.CoordinatorRoute

@Keep
sealed class PopUpRoute : CoordinatorRoute {

    @Suppress("LongParameterList")
    @Keep
    data class ShowConfirmationDialog(
        @StringRes val titleRes: Int? = null,
        val title: String? = null,
        @StringRes val descriptionRes: Int? = null,
        val description: String? = null,
        @StringRes val positiveButtonRes: Int? = null,
        val positiveButton: String? = null,
        @StringRes val negativeButtonRes: Int? = null,
        val negativeButton: String? = null,
        val requestKey: String? = null,
        val extras: Bundle? = null
    ) : PopUpRoute()

    @Suppress("LongParameterList")
    @Keep
    data class ShowErrorDialog(
        @StringRes val titleRes: Int? = R.string.common_dialog_error_component_title,
        val title: String? = null,
        val description: String? = null,
        @StringRes val descriptionRes: Int? = null,
        val requestKey: String? = null
    ) : PopUpRoute()
}
