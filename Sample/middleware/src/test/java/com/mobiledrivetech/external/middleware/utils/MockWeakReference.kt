package com.mobiledrivetech.external.middleware.utils

import java.lang.ref.WeakReference

class MockWeakReference<T>(initialValue: T? = null) : WeakReference<T>(null) {

    private var mockValue = initialValue

    override fun get(): T? {
        return mockValue
    }
}
