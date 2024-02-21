package com.inetpsa.pims.spaceMiddleware.util

import android.util.Base64
import com.inetpsa.mmx.foundation.monitoring.logger.PIMSLogger
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

internal class RSAxPSAEncryption {

    companion object {

        private const val KEY_FACTORY_ALGORITHM = "RSA"
        private const val KEY_CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding"
    }

    private val publicKey = """
        MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAL1XfbuMFyEIEM1V0ZMrL++Maw3gV+RA
        Po/P1LJt421/VTR1IzAJttqfpFFcXPsq/W5N8GfSO3JSzHyW2+ohe7kCAwEAAQ==
    """.trimIndent()

    private val publicKey2 = """
        MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnqPvPj9ZZ0V9vO3WHym4
        j6BIGxj5tUD1/B3lnPuF1E1tcRqPAef4kyHmG8NnLIwGsKh8cT/6dbdrWyH0u+8f
        EWyITdupASWsMWFknqe00WTUjpHRCgCctu32i5g42/lkkR8udcYv2oxiHcjfbK3h
        Zwvns7Y3hCYXnzAo3BMRsQqO/E9PX4D+JMmBDW54AKFVVMv/65QzGOd3zQTV2pZe
        HGkZq41+R2WfXb8NXdpeI1C1sOHo8qyD/h45JDmjq7rlkmvujRlp2IvTcgLHj/d9
        X/Yg1DKKpecuw/5NFKRq3UrULcc+GCvR+6A8t27LgFh1F6p4kk3YKSAqsmpn1wwv
        iQIDAQAB
    """.trimIndent()

    operator fun invoke(data: String, isUsingKey2: Boolean): String =
        try {
            val key = when (isUsingKey2) {
                true -> publicKey2
                false -> publicKey
            }
            val publicBytes = Base64.decode(key, Base64.DEFAULT)
            val keySpec = X509EncodedKeySpec(publicBytes)
            val keyFactory = KeyFactory.getInstance(KEY_FACTORY_ALGORITHM)
            val pubKey = keyFactory.generatePublic(keySpec)
            val cipher = Cipher.getInstance(KEY_CIPHER_ALGORITHM)
            cipher.init(Cipher.ENCRYPT_MODE, pubKey)
            val encodedBytes: ByteArray? = cipher.doFinal(data.toByteArray())
            Base64.encodeToString(encodedBytes, Base64.DEFAULT)
        } catch (@Suppress("TooGenericExceptionCaught") e: Exception) {
            PIMSLogger.e(e, "Error in Encryption: " + e.message)
            ""
        }
}
