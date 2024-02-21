package com.mobiledrivetech.external.sample.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
class TestDispatcherProvider(val dispatcher: TestDispatcher) :
    MiddlewareDispatcherProvider {

    override fun main(): CoroutineDispatcher = dispatcher

    override fun default(): CoroutineDispatcher = dispatcher

    override fun io(): CoroutineDispatcher = dispatcher

    override fun unconfined(): CoroutineDispatcher = dispatcher
}
