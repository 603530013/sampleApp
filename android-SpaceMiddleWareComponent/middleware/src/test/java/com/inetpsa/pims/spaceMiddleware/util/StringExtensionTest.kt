package com.inetpsa.pims.spaceMiddleware.util

import android.text.Html
import android.text.Spanned
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Test

class StringExtensionTest {

    @Test
    fun testCompareVersion() {
        // Test cases with non-null versions
        assertEquals(-1, "1.0.0".compareVersion("2.0.0"))
        assertEquals(1, "2.0.0".compareVersion("1.0.0"))
        assertEquals(0, "1.0.0".compareVersion("1.0.0"))
        assertEquals(1, "1.2.0".compareVersion("1.1.0"))
        assertEquals(-1, "1.1.0".compareVersion("1.2.0"))
        assertEquals(-1, "1.1.0".compareVersion("1.1.0.1"))
        assertEquals(0, "1.1".compareVersion("1.1.0"))
        assertEquals(0, "1".compareVersion("1.0.0"))

        // Test cases with nullable versions
        assertEquals(-1, null.compareVersion("1.0.0"))
        assertEquals(1, "1.0.0".compareVersion(null))
        assertEquals(0, null.compareVersion(null))

        // Test cases with blanc versions
        assertEquals(-1, " ".compareVersion("1.0.0"))
        assertEquals(1, "1.0.0".compareVersion(" "))
        assertEquals(0, " ".compareVersion("   "))
    }

    @Test
    fun `test isValidWebURL returns false if input is empty or null `() {
        val isValid = "".isValidWebUrl()
        assertFalse(isValid)
        val isValid1 = null.isValidWebUrl()
        assertFalse(isValid1)
    }

    @Test
    fun `test normaliseFromHtml`() {
        mockkStatic(Html::class)
        val spanned = mockk<Spanned>()
        every { Html.fromHtml("testString", Html.FROM_HTML_MODE_LEGACY) } returns spanned
        every { spanned.toString() } returns "testString"
        val result = "testString".normaliseFromHtml()
        assertEquals("testString", result.toString())
    }

    @Test
    fun `test normaliseFromHtml with empty string returns null`() {
        mockkStatic(Html::class)
        val spanned = mockk<Spanned>()
        every { Html.fromHtml("testString", Html.FROM_HTML_MODE_LEGACY) } returns spanned
        every { spanned.toString() } returns "testString"
        val result = "".normaliseFromHtml()
        assertNull(result)
    }

    @Test
    fun `test normaliseFromHtml trims a string properly`() {
        mockkStatic(Html::class)
        val spanned = mockk<Spanned>()
        every { Html.fromHtml("testString", Html.FROM_HTML_MODE_LEGACY) } returns spanned
        every { spanned.toString() } returns " testString "
        val result = "testString".normaliseFromHtml()
        assertEquals("testString", result.toString())
    }
}
