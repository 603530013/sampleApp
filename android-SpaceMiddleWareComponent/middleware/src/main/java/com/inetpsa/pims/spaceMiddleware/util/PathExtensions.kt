package com.inetpsa.pims.spaceMiddleware.util

import android.annotation.SuppressLint
import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import com.inetpsa.mmx.foundation.monitoring.logger.PIMSLogger
import java.io.File

private const val PATH_ASSETS_PREFIX = "assets://"
private const val PATH_RAW_PREFIX = "raw://"

internal inline fun <reified T> String.readAssetsJsonFile(context: Context): T? =
    when {
        isBlank() -> null

        startsWith(PATH_ASSETS_PREFIX) -> removePrefix(PATH_ASSETS_PREFIX)
            .takeIf { it.isNotBlank() }
            ?.let { readJsonFromAssets(context, it) }

        startsWith(PATH_RAW_PREFIX) -> removePrefix(PATH_RAW_PREFIX)
            .takeIf { it.isNotBlank() }
            ?.let { readJsonFromRawResources(context, it) }

        else -> readJsonFromFile(this)
    }
        ?.takeIf { it.isNotBlank() }
        ?.let { data ->
            try {
                val type = object : TypeToken<T>() {}.type
                Gson().fromJson<T>(data, type)
            } catch (ex: JsonParseException) {
                PIMSLogger.w(ex, "Exception when trying to read json file from $this")
                null
            }
        }

/**
 * Reads a file from the assets folder and returns its content as a String.
 *
 * @param filePath the path to the file to read
 * @return the content of the file as a String or null if the file could not be read
 */
@Suppress("TooGenericExceptionCaught")
internal fun readJsonFromAssets(context: Context, fileName: String): String? =
    try {
        context.assets.open(fileName).bufferedReader().use { it.readText() }
    } catch (ioException: Exception) {
        PIMSLogger.w(ioException, "trying to read assets: $fileName")
        null
    }

@Suppress("TooGenericExceptionCaught")
private fun readJsonFromFile(fileName: String): String? =
    try {
        File(fileName).readText()
    } catch (ioException: Exception) {
        PIMSLogger.w(ioException, "trying to read file: $fileName")
        null
    }

@SuppressLint("DiscouragedApi")
@Suppress("TooGenericExceptionCaught")
private fun readJsonFromRawResources(context: Context, fileName: String): String? =
    try {
        val resource = context.resources
        val rawId = resource.getIdentifier(fileName, "raw", context.packageName)
        resource.openRawResource(rawId).bufferedReader().use { it.readText() }
    } catch (ioException: Exception) {
        PIMSLogger.w(ioException, "trying to read raw resource: $fileName")
        null
    }
