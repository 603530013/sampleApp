package com.inetpsa.pims.spaceMiddleware.util

import android.util.Base64
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Assert
import org.junit.Test

class ByteArrayExtensionsTest {

    @Test
    fun `when getBase64 and byteFileArray is not empty then go to encodeToString and return it`() {
        mockkStatic(Base64::class)
        val encodeString = "test_encode"
        val byteFileArray: ByteArray = byteArrayOf(0, 1)
        every { Base64.encodeToString(any(), any()) } returns encodeString

        val occurrence = byteFileArray.getBase64()
        Assert.assertEquals(encodeString, occurrence)
        verify { Base64.encodeToString(byteFileArray, Base64.NO_WRAP) }
    }

    @Test
    fun `when getBase64 and byteFileArray is empty then return null`() {
        mockkStatic(Base64::class)
        val byteFileArray: ByteArray = byteArrayOf()

        val occurrence = byteFileArray.getBase64()
        Assert.assertEquals(null, occurrence)
        verify(exactly = 0) { Base64.encodeToString(any(), any()) }
    }

    @Test
    fun `when getBase64, byteFileArray is not empty and encodeToString throws Exception then return null`() {
        mockkStatic(Base64::class)
        val byteFileArray: ByteArray = byteArrayOf(0, 1)
        every { Base64.encodeToString(any(), any()) } throws Exception()
        val occurrence = byteFileArray.getBase64()
        Assert.assertEquals(null, occurrence)
        verify { Base64.encodeToString(any(), any()) }
    }
}
