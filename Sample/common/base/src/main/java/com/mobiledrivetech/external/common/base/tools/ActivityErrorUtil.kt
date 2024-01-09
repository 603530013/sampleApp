package com.mobiledrivetech.external.common.base.tools

import com.mobiledrivetech.external.common.base.coordinator.Router
import com.mobiledrivetech.external.common.base.extensions.kGet
import com.mobiledrivetech.external.common.base.routes.PopUpRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object ActivityErrorUtil {

    val dispatcher = kGet<DispatcherProvider>()

    fun showActivityNotFoundError(errorMessage: String) {
        CoroutineScope(dispatcher.io()).launch {
            Router follow PopUpRoute.ShowErrorDialog(
                description = errorMessage
            )
        }
    }
}
