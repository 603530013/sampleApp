package com.inetpsa.pims.spaceMiddleware.network

import android.content.Context
import com.inetpsa.mmx.foundation.networkManager.interceptor.ConnectivityInterceptor
import com.inetpsa.mmx.foundation.tools.Environment.DEV
import com.inetpsa.mmx.foundation.tools.Environment.GMA_PREPROD
import com.inetpsa.mmx.foundation.tools.Environment.PREPROD
import com.inetpsa.mmx.foundation.tools.Environment.PROD
import com.inetpsa.pims.spaceMiddleware.BuildConfig
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.verify
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.After
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import java.io.InputStream
import java.security.KeyStore
import java.security.cert.X509Certificate
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

class MiddlewareOkHttpClientTest {

    private lateinit var middlewareOkHttpClient: MiddlewareOkHttpClient
    private val context: Context = mockk()
    private val middlewareComponent: MiddlewareComponent = mockk()

    @Before
    fun setUp() {
        middlewareOkHttpClient = spyk(MiddlewareOkHttpClient())
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `test build`() {
        val defaultExpectedTimeout = 60_000
        val keyStore = mockk<KeyStore>()
        val sslSocketFactory = mockk<SSLSocketFactory>()
        val x509TrustManager = mockk<X509TrustManager>()
        every { middlewareComponent.context } returns context
        with(middlewareOkHttpClient) {
            every { handleCertificationPass(any()) } returns "".toCharArray()
            every { generateSslSocketFactory(any(), any()) } returns sslSocketFactory
            every { generateKeyStore(any(), any(), any()) } returns keyStore
            every { generateX509TrustManager(any()) } returns x509TrustManager
            every { x509TrustManager.acceptedIssuers } returns arrayOf<X509Certificate>()
        }

        val okHttpClient = middlewareOkHttpClient.build(middlewareComponent, DEV)

        with(okHttpClient) {
            assertEquals(defaultExpectedTimeout, readTimeoutMillis)
            assertEquals(defaultExpectedTimeout, connectTimeoutMillis)
            assertEquals(defaultExpectedTimeout, writeTimeoutMillis)
            assert(interceptors.isNotEmpty())
            assert(interceptors.size == 3)
            assert(interceptors[0] is ConnectivityInterceptor)
            assert(interceptors[1] is HttpLoggingInterceptor)
            assert(interceptors[2] is TokenInterceptor)
        }
    }

    @Test
    fun `test handleCertificationFile DEV environment`() {
        val devCertificationFile = "mymCertification/not_prod_MWPSP4A1.pfx"

        val devFile = middlewareOkHttpClient.handleCertificationFile(DEV)
        val preprodFile = middlewareOkHttpClient.handleCertificationFile(PREPROD)
        assertEquals(devCertificationFile, devFile)
        assertEquals(devCertificationFile, preprodFile)
    }

    @Test
    fun `test handleCertificationFile PROD environment`() {
        val prodCertificationFile = "mymCertification/prod_MZPSP4A1.pfx"
        val prodFile = middlewareOkHttpClient.handleCertificationFile(PROD)
        assertEquals(prodCertificationFile, prodFile)
    }

    @Test
    fun `test handleCertificationFile throws `() {
        assertThrows(IllegalArgumentException::class.java) {
            middlewareOkHttpClient.handleCertificationFile(GMA_PREPROD)
        }
    }

    @Test
    fun `test handleCertificationPass for dev environment`() {
        val nonProdPass = BuildConfig.MYM_NOT_PROD.toCharArray()
        assertArrayEquals(nonProdPass, middlewareOkHttpClient.handleCertificationPass(DEV))
        assertArrayEquals(nonProdPass, middlewareOkHttpClient.handleCertificationPass(PREPROD))
    }

    @Test
    fun `test handleCertificationPass for PROD environment`() {
        val prodPass = BuildConfig.MYM_PROD.toCharArray()
        assertArrayEquals(prodPass, middlewareOkHttpClient.handleCertificationPass(PROD))
    }

    @Test
    fun `test handleCertificationPass throws IllegalArgumentException`() {
        assertThrows(IllegalArgumentException::class.java) {
            middlewareOkHttpClient.handleCertificationPass(GMA_PREPROD)
        }
    }

    @Test
    fun `test generateX509TrustManager() returns X509TrustManager`() {
        val trustManagerFactory = mockk<TrustManagerFactory>()
        val keyStore = mockk<KeyStore>()
        val x509TrustManager = mockk<X509TrustManager>()
        val x509TrustManager2 = mockk<X509TrustManager>()
        mockkStatic(TrustManagerFactory::class)
        every { TrustManagerFactory.getInstance("X509") } returns trustManagerFactory
        every { trustManagerFactory.init(keyStore) } just Runs
        every { trustManagerFactory.trustManagers } returns arrayOf(x509TrustManager, mockk())

        val result = middlewareOkHttpClient.generateX509TrustManager(keyStore)
        assertEquals(x509TrustManager, result)

        every { trustManagerFactory.trustManagers } returns arrayOf(null, x509TrustManager2, x509TrustManager)

        val result2 = middlewareOkHttpClient.generateX509TrustManager(keyStore)
        assertEquals(x509TrustManager2, result2)
    }

    @Test
    fun `test generateX509TrustManager() throws error when no X509TrustManager`() {
        val trustManagerFactory = mockk<TrustManagerFactory>()
        val keyStore = mockk<KeyStore>()
        mockkStatic(TrustManagerFactory::class)
        every { TrustManagerFactory.getInstance("X509") } returns trustManagerFactory
        every { trustManagerFactory.init(keyStore) } just Runs
        every { trustManagerFactory.trustManagers } returns arrayOf()

        assertThrows(NoSuchElementException::class.java) {
            middlewareOkHttpClient.generateX509TrustManager(keyStore)
        }
    }

    /**
     * In this test just verifying that generateKeyStore() call all relavent methods
     */
    @Test
    fun `test generateKeyStore() calls all appropriate methods`() {
        val context = mockk<Context>()
        mockkStatic(KeyStore::class)
        val inputStream = mockk<InputStream>()
        every { KeyStore.getInstance("PKCS12").load(inputStream, charArrayOf()) } just Runs

        every { context.assets.open(any()) } returns inputStream
        middlewareOkHttpClient.generateKeyStore(context, DEV, charArrayOf())
        verify(exactly = 1) { middlewareOkHttpClient.handleCertificationFile(DEV) }
        verify(exactly = 1) { context.assets.open("mymCertification/not_prod_MWPSP4A1.pfx") }
        verify(exactly = 1) { KeyStore.getInstance("PKCS12") }
        verify(exactly = 1) { KeyStore.getInstance("PKCS12").load(inputStream, charArrayOf()) }
    }
}
