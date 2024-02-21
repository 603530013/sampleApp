package com.mobiledrivetech.external.sample.utils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.koin.dsl.binds
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.KoinTestRule

@OptIn(ExperimentalCoroutinesApi::class)
open class BaseTestKoin : AutoCloseKoinTest() {

    @get:Rule(order = 1)
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule(order = 2)
    val coroutineTestRule = CoroutineTestRule()

    @get:Rule(order = 3)
    val koinTestRule = KoinTestRule.create {
        // Your KoinApplication instance here
        modules(
            module {
                single { coroutineTestRule.provider } binds arrayOf(
                    MiddlewareDispatcherProvider::class
                )
            }
        )
    }
}
