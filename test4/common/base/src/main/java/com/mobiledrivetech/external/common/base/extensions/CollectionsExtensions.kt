package com.mobiledrivetech.external.common.base.extensions

/**
 * The extension checks if the list elements are consecutive
 * @return Boolean: If elements are consecutive, then returns true, else returns false
 */
@Suppress("ComplexMethod", "ReturnCount")
fun List<Int>?.hasConsecutiveItems(): Boolean {
    // if list is empty then return false
    if (isNullOrEmpty()) {
        return false
    }

    // get the minimum element in list, if we have null then return false
    val min: Int = minOrNull() ?: return false

    // get the maximum element in list, if we have null then return false
    val max: Int = maxOrNull() ?: return false

    // check if the min is equals the max then return false, we have the same item in list
    if (min == max) {
        return false
    }

    // check if the difference between min and max is equal to list size
    if (max - min + 1 != count()) {
        return false
    }

    // sort list and remove duplicate items
    val sortedList = this.toSortedSet()
    // check if we have the same count of lists
    if (count() != sortedList.count()) {
        return false
    }

    var prev = max
    // iterate through `TreeSet` and check if the difference between consecutive elements is 1
    // (Note that `TreeSet` stores the elements in sorted order)
    sortedList.forEach {
        if (prev != max && it != prev + 1) {
            return false
        }
        prev = it
    }
    return true
}
