package com.inetpsa.pims.spaceMiddleware.network

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.networkManager.interceptor.ConnectivityInterceptor
import com.inetpsa.mmx.foundation.tools.Environment
import com.inetpsa.pims.spaceMiddleware.BuildConfig
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.util.PimsOkHttpLogger
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.security.KeyStore
import java.security.SecureRandom
import java.util.concurrent.TimeUnit
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

internal class MiddlewareOkHttpClient {

    companion object {

        private const val DEFAULT_TIMEOUT_MYMARK = 60_000L
    }

    fun build(middlewareComponent: MiddlewareComponent, environment: Environment): OkHttpClient {
        val context = middlewareComponent.context
        val pass = handleCertificationPass(environment)
        val keyStore = generateKeyStore(context, environment, pass)

        val sslSocketFactory = generateSslSocketFactory(keyStore, pass)
        val trustManager = generateX509TrustManager(keyStore)

        return OkHttpClient().newBuilder()
            .apply {
                sslSocketFactory(sslSocketFactory, trustManager)
                readTimeout(DEFAULT_TIMEOUT_MYMARK, TimeUnit.MILLISECONDS)
                connectTimeout(DEFAULT_TIMEOUT_MYMARK, TimeUnit.MILLISECONDS)
                writeTimeout(DEFAULT_TIMEOUT_MYMARK, TimeUnit.MILLISECONDS)
                // Adding network connection Interceptor
                addInterceptor(ConnectivityInterceptor(context))

                val loggingInterceptor = HttpLoggingInterceptor(PimsOkHttpLogger()).apply {
                    this.setLevel(HttpLoggingInterceptor.Level.BODY)
                }
                addInterceptor(loggingInterceptor)
                addInterceptor(TokenInterceptor(middlewareComponent))
                authenticator(AuthenticatorExecutor(middlewareComponent))
            }
            .build()
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun handleCertificationPass(environment: Environment): CharArray = when (environment) {
        Environment.DEV, Environment.PREPROD -> BuildConfig.MYM_NOT_PROD.toCharArray()
        Environment.PROD -> BuildConfig.MYM_PROD.toCharArray()
        else -> throw IllegalArgumentException("environment ${environment.name} not supported with Mym BO")
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun handleCertificationFile(environment: Environment): String = when (environment) {
        Environment.DEV, Environment.PREPROD -> "mymCertification/not_prod_MWPSP4A1.pfx"
        Environment.PROD -> "mymCertification/prod_MZPSP4A1.pfx"
        else -> throw IllegalArgumentException("environment ${environment.name} not supported with Mym BO")
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun generateKeyStore(context: Context, environment: Environment, pass: CharArray): KeyStore {
        // Get the file of our certificate
        val path = handleCertificationFile(environment)
        val inputStream = context.assets.open(path)

        // We're going to put our certificates in a Keystore
        return KeyStore.getInstance("PKCS12").apply {
            load(inputStream, pass)
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun generateSslSocketFactory(keyStore: KeyStore, pass: CharArray): SSLSocketFactory {
        // Create a KeyManagerFactory with our specific algorithm of our public keys
        // Most of the cases is gonna be "X509"
        val keyManagerFactory = KeyManagerFactory.getInstance("X509")
        keyManagerFactory.init(keyStore, pass)

        // Create a SSL context with the key managers of the KeyManagerFactory
        val sslContext = SSLContext.getInstance("TLSv1.2")
        sslContext.init(keyManagerFactory.keyManagers, null, SecureRandom())
        return sslContext.socketFactory
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun generateX509TrustManager(keyStore: KeyStore): X509TrustManager {
        // Create a TrustManagerFactory with our specific algorithm of our public keys
        // Most of the cases is gonna be "X509"
        val trustManagerFactory = TrustManagerFactory.getInstance("X509")
        trustManagerFactory.init(keyStore)

        // get the generated X509TrustManager from the TrustManagerFactory
        return trustManagerFactory.trustManagers.toList()
            .filterIsInstance(X509TrustManager::class.java)
            .first()
    }
}
