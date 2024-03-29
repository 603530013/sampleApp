package com.mobiledrivetech.external.common.fundamental.extensions

import android.content.Intent
import android.content.res.Resources
import android.util.SparseArray
import android.view.MenuItem
import androidx.annotation.IdRes
import androidx.core.util.forEach
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mobiledrivetech.external.common.fundamental.R

/**
 * This is a workaround until the Navigation Component supports multiple back stacks.
 * Issue tracking: https://issuetracker.google.com/issues/127932815
 * Issue github: https://github.com/android/architecture-components-samples/issues/530
 */
@Suppress("LongMethod", "ComplexMethod", "LongParameterList")
fun BottomNavigationView.setupWithNavController(
    navGraphIds: List<Int>,
    selectedItemIndex: Int,
    fragmentManager: FragmentManager,
    containerId: Int,
    intent: Intent,
    onSelectItem: (MenuItem) -> Unit
): LiveData<NavController> {
    // Map of tags
    val graphIdToTagMap = SparseArray<String>()
    // Result. Mutable live data with the selected controlled
    val selectedNavController = MutableLiveData<NavController>()

    var firstFragmentGraphId = 0

    // First create a NavHostFragment for each NavGraph ID
    navGraphIds.forEachIndexed { index, navGraphId ->
        val fragmentTag = getFragmentTag(index)

        // Find or create the Navigation host fragment
        val navHostFragment = obtainNavHostFragment(fragmentManager, fragmentTag, navGraphId, containerId)

        // Obtain its id
        val graphId = navHostFragment.navController.graph.id

        if (index == selectedItemIndex) {
            firstFragmentGraphId = graphId
            selectedItemId = graphId
        }

        // Save to the map
        graphIdToTagMap.put(graphId, fragmentTag)

        // Attach or detach nav host fragment depending on whether it's the selected item.
        when (index == selectedItemIndex) {
            true -> {
                // Update liveData with the selected graph
                selectedNavController.value = navHostFragment.navController
                attachNavHostFragment(fragmentManager, navHostFragment, index == 0)
            }

            else -> detachNavHostFragment(fragmentManager, navHostFragment)
        }
    }

    // Now connect selecting an item with swapping Fragments
    var selectedItemTag = graphIdToTagMap.get(selectedItemId)
    val firstFragmentTag = graphIdToTagMap.get(firstFragmentGraphId)
    var isOnFirstFragment = selectedItemTag == firstFragmentTag

    // When a navigation item is selected
    setOnNavigationItemSelectedListener { item ->
        onSelectItem(item)
        // Don't do anything if the state is state has already been saved.
        val newlySelectedItemTag = graphIdToTagMap.get(item.itemId)
        when {
            fragmentManager.isStateSaved -> false

            selectedItemTag != newlySelectedItemTag -> handleNavigationToSelectedItem(
                fragmentManager = fragmentManager,
                firstFragmentTag = firstFragmentTag,
                newlySelectedItemTag = newlySelectedItemTag,
                graphIdToTagMap = graphIdToTagMap
            ) { newSelectedItemTag, onFirstFragment, navController ->
                selectedItemTag = newSelectedItemTag
                isOnFirstFragment = onFirstFragment
                selectedNavController.value = navController
            }

            else -> false
        }
    }

    // Optional: on item reselected, pop back stack to the destination of the graph
    setupItemReselected(
        graphIdToTagMap = graphIdToTagMap,
        fragmentManager = fragmentManager,
        firstFragmentTag = firstFragmentTag
    ) { newSelectedItemTag, onFirstFragment, navController ->
        selectedItemTag = newSelectedItemTag
        isOnFirstFragment = onFirstFragment
        selectedNavController.value = navController
    }

    // Handle deep link
    setupDeepLinks(navGraphIds, fragmentManager, containerId, intent)

    // Finally, ensure that we update our BottomNavigationView when the back stack changes
    fragmentManager.addOnBackStackChangedListener {
        if (!isOnFirstFragment && !fragmentManager.isOnBackStack(firstFragmentTag)) {
            this.selectedItemId = firstFragmentGraphId
        }

        // Reset the graph if the currentDestination is not valid (happens when the back
        // stack is popped after using the back button).
        selectedNavController.value?.let { controller ->
            if (controller.currentDestination == null) {
                controller.navigate(controller.graph.id)
            }
        }
    }
    return selectedNavController
}

private fun handleNavigationToSelectedItem(
    fragmentManager: FragmentManager,
    firstFragmentTag: String,
    newlySelectedItemTag: String,
    graphIdToTagMap: SparseArray<String>,
    action: (String, Boolean, NavController) -> Unit
): Boolean {
    // Pop everything above the first fragment (the "fixed start destination")
    fragmentManager.popBackStack(firstFragmentTag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    return (fragmentManager.findFragmentByTag(newlySelectedItemTag) as? NavHostFragment)?.let { selectedFragment ->
        // Exclude the first fragment tag because it's always in the back stack.
        if (firstFragmentTag != newlySelectedItemTag) {
            // Commit a transaction that cleans the back stack and adds the first fragment
            // to it, creating the fixed started destination.
            makeFragmentTransaction(
                fragmentManager = fragmentManager,
                selectedFragment = selectedFragment,
                firstFragmentTag = firstFragmentTag,
                newlySelectedItemTag = newlySelectedItemTag,
                graphIdToTagMap = graphIdToTagMap
            )
        }
        action(newlySelectedItemTag, newlySelectedItemTag == firstFragmentTag, selectedFragment.navController)
        true
    } ?: false
}

private fun makeFragmentTransaction(
    fragmentManager: FragmentManager,
    selectedFragment: Fragment,
    firstFragmentTag: String,
    newlySelectedItemTag: String,
    graphIdToTagMap: SparseArray<String>
) {
    val transaction = fragmentManager.beginTransaction()
    transaction.attach(selectedFragment)
    transaction.setPrimaryNavigationFragment(selectedFragment)
    // Detach all other Fragments
    graphIdToTagMap.forEach { _, fragmentTagIter ->
        if (fragmentTagIter != newlySelectedItemTag) {
            fragmentManager.findFragmentByTag(firstFragmentTag)?.let { transaction.detach(it) }
        }
    }
    transaction.addToBackStack(firstFragmentTag)
    transaction.setReorderingAllowed(true)
    transaction.commit()
}

private fun BottomNavigationView.setupDeepLinks(
    navGraphIds: List<Int>,
    fragmentManager: FragmentManager,
    containerId: Int,
    intent: Intent
) {
    navGraphIds.forEachIndexed { index, navGraphId ->
        val fragmentTag = getFragmentTag(index)

        // Find or create the Navigation host fragment
        val navHostFragment = obtainNavHostFragment(
            fragmentManager,
            fragmentTag,
            navGraphId,
            containerId
        )
        // Handle Intent
        val graphId = navHostFragment.navController.graph.id
        if (navHostFragment.navController.handleDeepLink(intent) && selectedItemId != graphId) {
            this.selectedItemId = navHostFragment.navController.graph.id
        }
    }
}

private fun BottomNavigationView.setupItemReselected(
    graphIdToTagMap: SparseArray<String>,
    fragmentManager: FragmentManager,
    firstFragmentTag: String,
    action: (String, Boolean, NavController) -> Unit
) {
    setOnNavigationItemReselectedListener { item ->
        val newlySelectedItemTag = graphIdToTagMap[item.itemId]
        (fragmentManager.findFragmentByTag(newlySelectedItemTag) as? NavHostFragment)?.let { selectedFragment ->
            val navController = selectedFragment.navController
            // Pop the back stack to the start destination of the current navController graph
            if (!navController.popBackStack(navController.graph.startDestinationId, false)) {
                handleNavigationToSelectedItem(
                    fragmentManager = fragmentManager,
                    firstFragmentTag = firstFragmentTag,
                    newlySelectedItemTag = newlySelectedItemTag,
                    graphIdToTagMap = graphIdToTagMap,
                    action = action
                )
            }
        }
    }
}

private fun detachNavHostFragment(fragmentManager: FragmentManager, navHostFragment: NavHostFragment) {
    fragmentManager.beginTransaction()
        .detach(navHostFragment)
        .commitNow()
}

private fun attachNavHostFragment(
    fragmentManager: FragmentManager,
    navHostFragment: NavHostFragment,
    isPrimaryNavFragment: Boolean
) {
    fragmentManager.beginTransaction()
        .attach(navHostFragment)
        .apply {
            if (isPrimaryNavFragment) {
                setPrimaryNavigationFragment(navHostFragment)
            }
        }
        .commitNow()
}

private fun obtainNavHostFragment(
    fragmentManager: FragmentManager,
    fragmentTag: String,
    navGraphId: Int,
    containerId: Int
): NavHostFragment {
    // If the Nav Host fragment exists, return it
    (fragmentManager.findFragmentByTag(fragmentTag) as? NavHostFragment)?.let { existingFragment ->
        return existingFragment
    }

    // Otherwise, create it and return it.
    val navHostFragment = NavHostFragment.create(navGraphId)
    fragmentManager.beginTransaction()
        .add(containerId, navHostFragment, fragmentTag)
        .detach(navHostFragment)
        .commitNow()
    return navHostFragment
}

private fun FragmentManager.isOnBackStack(backStackName: String): Boolean {
    val backStackCount = backStackEntryCount
    for (index in 0 until backStackCount) {
        if (getBackStackEntryAt(index).name == backStackName) {
            return true
        }
    }
    return false
}

private fun getFragmentTag(index: Int) = "bottomNavigation#$index"

// find one nav controller by fragment id
fun Fragment.findNavControllerById(@IdRes id: Int): NavController {
    var parent = parentFragment
    while (parent != null) {
        if (parent is NavHostFragment && parent.id == id) {
            return parent.navController
        }
        parent = parent.parentFragment
    }
    throw Resources.NotFoundException("NavController with specified id not found")
}

// find one nav controller by fragment id
fun Fragment.findNavControllerByFragmentViewId(@IdRes id: Int): NavController =
    (activity?.supportFragmentManager?.findFragmentById(id) as? NavHostFragment)?.navController
        ?: throw Resources.NotFoundException("NavController with specified id not found")

fun Fragment.activityNavController() = findNavControllerById(R.id.navigation_host_activity)
