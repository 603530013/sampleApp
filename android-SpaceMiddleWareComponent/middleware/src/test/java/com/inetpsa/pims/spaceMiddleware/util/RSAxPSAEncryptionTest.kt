package com.inetpsa.pims.spaceMiddleware.util

import android.util.Base64
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.security.KeyFactory
import java.security.PublicKey
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException

class RSAxPSAEncryptionTest {

    // Using the same key mentioned in RSAxPSAEncryption for testing
    private val publicKey = """
        MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAL1XfbuMFyEIEM1V0ZMrL++Maw3gV+RA
        Po/P1LJt421/VTR1IzAJttqfpFFcXPsq/W5N8GfSO3JSzHyW2+ohe7kCAwEAAQ==
    """.trimIndent()

    // Using the same key mentioned in RSAxPSAEncryption for testing
    private val publicKey2 = """
        MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnqPvPj9ZZ0V9vO3WHym4
        j6BIGxj5tUD1/B3lnPuF1E1tcRqPAef4kyHmG8NnLIwGsKh8cT/6dbdrWyH0u+8f
        EWyITdupASWsMWFknqe00WTUjpHRCgCctu32i5g42/lkkR8udcYv2oxiHcjfbK3h
        Zwvns7Y3hCYXnzAo3BMRsQqO/E9PX4D+JMmBDW54AKFVVMv/65QzGOd3zQTV2pZe
        HGkZq41+R2WfXb8NXdpeI1C1sOHo8qyD/h45JDmjq7rlkmvujRlp2IvTcgLHj/d9
        X/Yg1DKKpecuw/5NFKRq3UrULcc+GCvR+6A8t27LgFh1F6p4kk3YKSAqsmpn1wwv
        iQIDAQAB
    """.trimIndent()
    private lateinit var rsAxPSAEncryption: RSAxPSAEncryption

    @Before
    fun setUp() {
        rsAxPSAEncryption = RSAxPSAEncryption()
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `test invoke() with some exception returns empty string`() {
        val keyCipherAlgorithm = "RSA/ECB/PKCS1Padding"
        mockkStatic(Cipher::class)
        every { Cipher.getInstance(keyCipherAlgorithm) } returns mockk() {
            every { doFinal(any()) } throws IllegalBlockSizeException("Illegal size")
        }
        val result = rsAxPSAEncryption.invoke("sampleData", false)
        assertEquals("", result)
    }

    @Test
    fun `test invoke() encrypts given data properly with publicKey1`() {
        val keyFactoryAlgorithm = "RSA"
        val keyCipherAlgorithm = "RSA/ECB/PKCS1Padding"
        val sampleData = "Sample Data"
        val encodedBytes = "encodedData".toByteArray()
        val expectedEncodedData = "expectedEncodedData"
        val publicKeyMock = mockk<PublicKey>()
        mockkStatic(Cipher::class)
        mockkStatic(KeyFactory::class)
        mockkStatic(Base64::class)
        every { Base64.decode(any<String>(), Base64.DEFAULT) } returns publicKey.toByteArray()

        every { KeyFactory.getInstance(keyFactoryAlgorithm) } returns mockk() {
            every { generatePublic(any()) } returns publicKeyMock
        }
        every { Cipher.getInstance(keyCipherAlgorithm) } returns mockk() {
            every { init(Cipher.ENCRYPT_MODE, publicKeyMock) } just Runs
            every { doFinal(sampleData.toByteArray()) } returns encodedBytes
        }
        every { Base64.encodeToString(encodedBytes, Base64.DEFAULT) } returns expectedEncodedData

        val result = rsAxPSAEncryption.invoke(sampleData, false)
        verify { Base64.decode(publicKey, Base64.DEFAULT) }
        verify { KeyFactory.getInstance(keyFactoryAlgorithm) }
        verify { Cipher.getInstance(keyCipherAlgorithm) }
        assertEquals(expectedEncodedData, result)
    }

    @Test
    fun `test invoke() encrypts given data properly with publicKey2`() {
        val keyFactoryAlgorithm = "RSA"
        val keyCipherAlgorithm = "RSA/ECB/PKCS1Padding"
        val sampleData = "Sample Data"
        val encodedBytes = "encodedData".toByteArray()
        val expectedEncodedData = "expectedEncodedData"
        val publicKeyMock = mockk<PublicKey>()
        mockkStatic(Cipher::class)
        mockkStatic(KeyFactory::class)
        mockkStatic(Base64::class)
        every { Base64.decode(any<String>(), Base64.DEFAULT) } returns publicKey2.toByteArray()

        every { KeyFactory.getInstance(keyFactoryAlgorithm) } returns mockk() {
            every { generatePublic(any()) } returns publicKeyMock
        }
        every { Cipher.getInstance(keyCipherAlgorithm) } returns mockk() {
            every { init(Cipher.ENCRYPT_MODE, publicKeyMock) } just Runs
            every { doFinal(sampleData.toByteArray()) } returns encodedBytes
        }
        every { Base64.encodeToString(encodedBytes, Base64.DEFAULT) } returns expectedEncodedData

        val result = rsAxPSAEncryption.invoke(sampleData, true)
        verify { Base64.decode(publicKey2, Base64.DEFAULT) }
        verify { KeyFactory.getInstance(keyFactoryAlgorithm) }
        verify { Cipher.getInstance(keyCipherAlgorithm) }
        assertEquals(expectedEncodedData, result)
    }
}
