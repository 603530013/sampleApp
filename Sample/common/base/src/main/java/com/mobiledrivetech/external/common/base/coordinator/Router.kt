package com.mobiledrivetech.external.common.base.coordinator

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * The [Router] can be used in a view independent class to follow a specific [CoordinatorRoute].
 * A flow specific [Coordinator] then uses the [Router] to determine where to navigate.
 */
object Router {

    private val routeSubject: MutableSharedFlow<CoordinatorRoute> = MutableSharedFlow()

    /**
     * This is used to observe which [CoordinatorRoute] to handle in a [Coordinator].
     */
    val routes: Flow<CoordinatorRoute> = routeSubject

    /**
     * Use this to follow a specific [CoordinatorRoute].
     */
    suspend infix fun <RouteToFollow : CoordinatorRoute> follow(route: RouteToFollow) {
        routeSubject.emit(route)
    }
}
