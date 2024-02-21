package com.inetpsa.pims.spaceMiddleware.util

import com.inetpsa.mmx.foundation.monitoring.ErrorCode
import com.inetpsa.mmx.foundation.monitoring.ErrorMessage
import io.mockk.spyk
import org.junit.Assert
import org.junit.Test

class PimsErrorsTest {

    private val pimsErrors: PimsErrors = spyk()

    @Test
    fun `when needPinToken then return its PIMSError`() {
        val actual = pimsErrors.strongAuth
        Assert.assertEquals(ErrorCode.needStrongAuth, actual.code)
        Assert.assertEquals(ErrorMessage.needStrongAuth, actual.message)
    }

    @Test
    fun `when apiNotSupported then return its PIMSError`() {
        val actual = pimsErrors.apiNotSupported()
        Assert.assertEquals(ErrorCode.apiNotSupported, actual.code)
        Assert.assertEquals(ErrorMessage.apiNotSupported, actual.message)
    }

    @Test
    fun `when updateProfileFailed then return its PIMSError`() {
        val actual = pimsErrors.updateProfileFailed()
        Assert.assertEquals(2001, actual.code)
        Assert.assertTrue(actual.message.contains("update profile failed"))
    }

    @Test
    fun `when serverError with errors then return its serverError`() {
        val errors = listOf("error_1", "error_2")
        val actual = pimsErrors.serverError(errors, "fallback_error")
        actual.subError?.let {
            Assert.assertEquals(2006, it.status)
            Assert.assertEquals(errors.toString(), it.body)
        }
    }

    @Test
    fun `when serverError with no errors then return its serverError`() {
        val actual = pimsErrors.serverError(null, "fallback_error")
        actual.subError?.let {
            Assert.assertEquals(2006, it.status)
            Assert.assertEquals("fallback_error", it.body)
        }
    }

    @Test
    fun `when zeroResults with no message then return its PIMSError`() {
        val actual = pimsErrors.zeroResults()
        Assert.assertEquals(2003, actual.code)
        Assert.assertEquals("sorry no response found", actual.message)
    }

    @Test
    fun `when zeroResults with message then return its PIMSError`() {
        val actual = pimsErrors.zeroResults("zero_result_error")
        Assert.assertEquals(2003, actual.code)
        Assert.assertEquals("zero_result_error", actual.message)
    }

    @Test
    fun `when locationServerError with no message then return its PIMSError`() {
        val actual = pimsErrors.locationServerError()
        actual.subError?.let {
            Assert.assertEquals(2004, it.status)
            Assert.assertEquals("sorry server error", it.body)
        }
    }

    @Test
    fun `when locationServerError with message then return its PIMSError`() {
        val actual = pimsErrors.locationServerError("location_server_error")
        actual.subError?.let {
            Assert.assertEquals(2004, it.status)
            Assert.assertEquals("location_server_error", it.body)
        }
    }

    @Test
    fun `when invalidServerUrl with message then return its PIMSError`() {
        val url = "invalid_url"
        val actual = pimsErrors.invalidServerUrl(url)
        Assert.assertEquals(2005, actual.code)
        Assert.assertEquals("invalid server url: $url", actual.message)
    }

    @Test
    fun `when typeError then return its PIMSError`() {
        val actual = pimsErrors.typeError("type_error")
        Assert.assertEquals(2007, actual.code)
        Assert.assertEquals("type_error", actual.message)
    }
}
