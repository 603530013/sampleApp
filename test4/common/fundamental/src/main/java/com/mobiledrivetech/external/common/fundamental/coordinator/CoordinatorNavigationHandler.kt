package com.mobiledrivetech.external.common.fundamental.coordinator

import androidx.fragment.app.Fragment

interface CoordinatorNavigationHandler {
    /**
     * Attaches a navigation handler to this [Coordinator] that is used to handle navigation.
     */
    infix fun provideNavigationHandler(navigationHandler: Fragment?)

    /**
     * clear references
     */
    fun onCleared()
}
