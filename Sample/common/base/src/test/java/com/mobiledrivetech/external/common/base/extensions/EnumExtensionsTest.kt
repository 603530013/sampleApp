package com.mobiledrivetech.external.common.base.extensions

import org.junit.Assert
import org.junit.Test

class EnumExtensionsTest {

    enum class TestEnum {
        FIRST,
        SECOND,
        THIRD
    }

    @Test
    fun `safeValueOf returns correct enum for valid name`() {
        val result = safeValueOf<TestEnum>("FIRST")
        Assert.assertEquals(TestEnum.FIRST, result)
    }

    @Test
    fun `safeValueOf returns null for invalid name`() {
        val result = safeValueOf<TestEnum>("FOURTH")
        Assert.assertNull(result)
    }

    @Test
    fun `safeValueOf returns null for empty name`() {
        val result = safeValueOf<TestEnum>("")
        Assert.assertNull(result)
    }

    @Test
    fun `safeValueOf returns null for valid name with incorrect case`() {
        val result = safeValueOf<TestEnum>("first")
        Assert.assertNull(result)
    }
}