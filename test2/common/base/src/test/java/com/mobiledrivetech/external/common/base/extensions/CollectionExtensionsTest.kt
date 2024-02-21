package com.mobiledrivetech.external.common.base.extensions

import org.junit.Assert
import org.junit.Test

class CollectionExtensionsTest {

    @Test
    fun `when hasConsecutiveItems with null returns false`() {
        val list: List<Int>? = null
        Assert.assertFalse(list.hasConsecutiveItems())
    }

    @Test
    fun `when hasConsecutiveItems with empty list returns false`() {
        val emptyList = emptyList<Int>()
        Assert.assertFalse(emptyList.hasConsecutiveItems())
    }

    @Test
    fun `when hasConsecutiveItems with one element returns false`() {
        val singleItemList = listOf(1)
        Assert.assertFalse(singleItemList.hasConsecutiveItems())
    }

    @Test
    fun `when hasConsecutiveItems with all elements same returns false`() {
        val sameElementsList = listOf(1, 1, 1, 1)
        Assert.assertFalse(sameElementsList.hasConsecutiveItems())
    }

    @Test
    fun `when hasConsecutiveItems with non-consecutive elements returns false`() {
        val nonConsecutiveList = listOf(1, 3, 5, 7)
        Assert.assertFalse(nonConsecutiveList.hasConsecutiveItems())
    }

    @Test
    fun `when hasConsecutiveItems with consecutive elements returns true`() {
        val consecutiveList = listOf(1, 2, 3, 4)
        Assert.assertTrue(consecutiveList.hasConsecutiveItems())
    }

    @Test
    fun `when hasConsecutiveItems with consecutive elements but including duplicates returns false`() {
        val consecutiveWithDuplicates = listOf(1, 2, 2, 3, 4)
        Assert.assertFalse(consecutiveWithDuplicates.hasConsecutiveItems())
    }

    @Test
    fun `hasConsecutiveItems with duplicates returns false`() {
        val listWithDuplicates = listOf(1, 2, 2, 3, 5)
        Assert.assertFalse(listWithDuplicates.hasConsecutiveItems())
    }
}
