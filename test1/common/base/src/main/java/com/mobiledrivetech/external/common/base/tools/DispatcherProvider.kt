package com.mobiledrivetech.external.common.base.tools

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherProvider {
    fun main(): CoroutineDispatcher
    fun default(): CoroutineDispatcher

    @Suppress("FunctionMinLength")
    fun io(): CoroutineDispatcher
    fun unconfined(): CoroutineDispatcher
}