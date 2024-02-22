package com.mobiledrivetech.external.common.fundamental.coordinator

import android.os.Bundle
import android.util.Log
import androidx.annotation.CallSuper
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.mobiledrivetech.external.common.base.tools.DispatcherProvider
import com.mobiledrivetech.external.common.fundamental.R
import com.mobiledrivetech.external.common.fundamental.extensions.NavigationType
import com.mobiledrivetech.external.common.fundamental.extensions.findNavControllerById
import com.mobiledrivetech.external.common.fundamental.extensions.navigate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference

/**
 * A [Coordinator] handles navigation or view flow for one or more view controllers (e.g.
 * [androidx.fragment.app.Fragment], [android.app.Activity], [android.view.ViewGroup], [android.view.View]). Its purpose
 * is to isolate navigation logic.
 *
 * The [Route] must implement [CoordinatorRoute] and defines the routes that the coordinator can navigate to with the
 * help of a [NavigationHandler].
 */
abstract class Coordinator<Route : CoordinatorRoute, NavigationHandler : Fragment>(dispatcher: DispatcherProvider) :
    CoordinatorNavigationHandler {

    private var weakHandler: WeakReference<NavigationHandler?>? = null

    val handler: NavigationHandler?
        get() = weakHandler?.get()

    val navController: NavController?
        get() = handler?.findNavController()

    private var coScopeJob: Job? = null
    private val CLASS_NAME = javaClass.simpleName

    init {
        CoroutineScope(dispatcher.io() + Job()).launch {
            Router.routes
                .onEach {
                    Log.d(CLASS_NAME, "Router.routes: $it")
                }
                .flowOn(dispatcher.default())
                .catch {
                    //SpaceLogger.w(it) 
                }
                .onEach { route -> handleNavigation(dispatcher, route) }
                .launchIn(this)
        }
    }

    private suspend fun handleNavigation(dispatcher: DispatcherProvider, route: CoordinatorRoute) {
        @Suppress("TooGenericExceptionCaught")
        try {
            @Suppress("UNCHECKED_CAST")
            val coordinatorRoute: Route = route as Route
            navigate(dispatcher, coordinatorRoute)
        } catch (ex: Exception) {
            Log.w(CLASS_NAME, "${ex.cause}, ${ex.localizedMessage}")
            // route is not of type CoordinatorRoute --> don't navigate
            ex.printStackTrace()
        }
    }

    /** Attaches a navigation handler to this [Coordinator] that is used to handle navigation. */
    override infix fun provideNavigationHandler(navigationHandler: Fragment?) {
        @Suppress("UNCHECKED_CAST")
        weakHandler = WeakReference(navigationHandler as? NavigationHandler)
        Log.d(CLASS_NAME,
            "******* calling provideNavigationHandler. creating handler for ${navigationHandler?.javaClass?.name}"
        )
        coScopeJob?.cancel()
        coScopeJob = CoroutineScope(Dispatchers.IO).launch {
            onProviderNavigationHandlerSet()
        }
    }

    /** provideNavigationHandler Method that handles the navigation that is defined through a [Route]. */
    abstract suspend fun navigate(dispatcher: DispatcherProvider, route: Route)

    /** utility function that is called to notify when the handler has been set in this Coordinator**/
    abstract suspend fun onProviderNavigationHandlerSet()

    /** clear references */
    @CallSuper
    override fun onCleared() {
        weakHandler = null
        coScopeJob?.cancel()
    }

    suspend fun navigate(
        navigationType: NavigationType,
        dispatcher: DispatcherProvider,
        @IdRes id: Int,
        bundle: Bundle? = null
    ) {
        withContext(dispatcher.main()) {
            val currentNode: NavDestination = navController?.currentDestination
                ?: navController?.graph ?: error("no current navigation node")
            val navOptions: NavOptions? = currentNode.getAction(id)?.navOptions
            navController?.navigate(navigationType, id, bundle) {
                navOptions?.let {
                    setLaunchSingleTop(it.shouldLaunchSingleTop())
                    setPopUpTo(it.popUpToId, it.isPopUpToInclusive())
                } ?: this
            }
        }
    }

    suspend fun navigateWithActivityHost(
        navigationType: NavigationType,
        dispatcher: DispatcherProvider,
        @IdRes id: Int,
        bundle: Bundle? = null,
        @IdRes destinationId: Int? = 0
    ) {
        if (navController?.currentDestination?.id == destinationId) {
            Log.d(CLASS_NAME, "Same as the ID of the currentDestination id")
            return
        }
        withContext(dispatcher.main()) {
            val navController = handler?.findNavControllerById(R.id.navigation_host_activity)
            val currentNode: NavDestination = navController?.currentDestination
                ?: navController?.graph ?: error("no current navigation node")
            val navOptions: NavOptions? = currentNode.getAction(id)?.navOptions
            navController?.navigate(navigationType, id, bundle) {
                navOptions?.let {
                    setLaunchSingleTop(it.shouldLaunchSingleTop())
                    setPopUpTo(it.popUpToId, it.isPopUpToInclusive())
                } ?: this
            }
        }
    }

    suspend fun popBackStack(dispatcher: DispatcherProvider): Boolean =
        withContext(dispatcher.main()) { navController?.popBackStack() ?: false }

    suspend fun popBackStack(
        dispatcher: DispatcherProvider,
        @IdRes id: Int,
        inclusive: Boolean = false
    ): Boolean =
        withContext(dispatcher.main()) { navController?.popBackStack(id, inclusive) ?: false }
}
