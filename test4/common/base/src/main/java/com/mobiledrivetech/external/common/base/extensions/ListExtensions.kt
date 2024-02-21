package com.mobiledrivetech.external.common.base.extensions

inline fun <T> List<T>.indexOfFirstOrNull(predicate: (T) -> Boolean): Int? {
    return indexOfFirst(predicate).takeIf { it != -1 }
}
