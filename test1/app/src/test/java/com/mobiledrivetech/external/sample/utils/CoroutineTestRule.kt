package com.mobiledrivetech.external.sample.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class CoroutineTestRule : TestWatcher() {

    val provider: TestDispatcherProvider = TestDispatcherProvider(UnconfinedTestDispatcher())

    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(provider.main())
    }

    override fun finished(description: Description) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}
