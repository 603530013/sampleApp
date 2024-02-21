package com.mobiledrivetech.external.common.base.extensions

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ListExtensionsTest {
    @Test
    fun `indexOfFirstOrNull returns index of first matching element`() {
        val list = listOf("apple", "banana", "cherry")
        val index = list.indexOfFirstOrNull { it.startsWith("b") }
        assertEquals(1, index)
    }

    @Test
    fun `indexOfFirstOrNull returns null when no element matches`() {
        val list = listOf("apple", "banana", "cherry")
        val index = list.indexOfFirstOrNull { it.startsWith("z") }
        assertNull(index)
    }

    @Test
    fun `indexOfFirstOrNull returns null for empty list`() {
        val list = emptyList<String>()
        val index = list.indexOfFirstOrNull { it.startsWith("a") }
        assertNull(index)
    }

    @Test
    fun `indexOfFirstOrNull returns index of first matching element in case of multiple matches`() {
        val list = listOf("apple", "apricot", "banana")
        val index = list.indexOfFirstOrNull { it.startsWith("a") }
        assertEquals(0, index)
    }
}