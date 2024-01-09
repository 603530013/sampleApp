package com.mobiledrivetech.external.common.base.coordinator

/**
 * A [CoordinatorRoute] expresses a way that a Coordinator can follow for navigation.
 *
 * It is not up to the [CoordinatorRoute] to decide where or how to navigate. Do not couple view flows to a
 * [CoordinatorRoute], rather try to define a coordinated action as [CoordinatorRoute].
 * e.g. "ShowIsPicked(show: Show)" instead of "OpenShowDetailViewWith(show: Show)"
 */
interface CoordinatorRoute
