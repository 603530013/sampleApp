package com.inetpsa.pims.spaceMiddleware.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.monitoring.logger.PIMSLogger
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.Locale
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

internal class FileSplitter {

    companion object {

        private const val MAX_LIMIT_SIZE = 7864320 // max limit in 7.5MB before baseTo64
        private const val EACH_FILE_TIMES = 7680 // 0.75*10*1024 = 7860
        private const val BUFFER_SIZE = 1024
    }

    /**
     * this function is to handle big file(>10MB) to smaller ones
     * 1. zip it as fileName.zip
     * 2. after zip, if it is still over 10MB then split it
     * */
    internal fun handleUploadFile(path: String, fileName: String, context: Context): List<String> {
        val uri = Uri.parse(path)
        val size = getFileByteWithUri(uri, context).size
        PIMSLogger.i("start handle uri data, fileName = $fileName")
        return if (size > MAX_LIMIT_SIZE) {
            val downloadDirectory = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            val zipName = fileName.substringBefore(".")
            val zipFile = File(downloadDirectory, "$zipName.zip")
            if (fileName.endsWith(".zip")) {
                copy(uri, context, zipName, downloadDirectory)
                split(zipFile, downloadDirectory)
            } else {
                zip(context, uri, zipFile, zipName)
                if (zipFile.length() > MAX_LIMIT_SIZE) {
                    split(zipFile, downloadDirectory)
                } else {
                    listOf(zipFile.absolutePath)
                }
            }
        } else {
            PIMSLogger.i("no need to split or zip file")
            listOf(uri.toString())
        }
    }

    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    internal fun handleUploadImage(path: String, quality: Int, context: Context): ByteArray =
        try {
            val uri = Uri.parse(path)
            context.contentResolver.openInputStream(uri)
                ?.use { inputStream ->
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    val byteArrayOutputStream = ByteArrayOutputStream()
                    val format = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                        Bitmap.CompressFormat.WEBP
                    } else {
                        Bitmap.CompressFormat.WEBP_LOSSY
                    }
                    bitmap?.compress(format, quality, byteArrayOutputStream)
                    byteArrayOutputStream.toByteArray()
                } ?: ByteArray(0)
        } catch (e: Exception) {
            ByteArray(0)
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun split(zipFile: File, downloadDirectory: File?): List<String> {
        PIMSLogger.i("Start to split $zipFile")
        val filesPath = mutableListOf<String>()
        try {
            val bufferedInputStream = BufferedInputStream(FileInputStream(zipFile))
            val fileSize = zipFile.length()
            val chunkSize = fileSize / MAX_LIMIT_SIZE
            val buffer = ByteArray(BUFFER_SIZE)
            // split files as names.z01, names.z02, etc. and each of them limit in 10MB * 0.75
            val preSubFileName = zipFile.nameWithoutExtension + ".z"
            for (subFile in 1..chunkSize) {
                val extensionSuffix = String.format(Locale.ENGLISH, "%03d", subFile)
                val subZipFile = File(downloadDirectory, "$preSubFileName$extensionSuffix")
                val bufferedOutputStream = BufferedOutputStream(FileOutputStream(subZipFile))
                var currentTimes = 1
                while (currentTimes <= EACH_FILE_TIMES) {
                    bufferedOutputStream.write(buffer, 0, bufferedInputStream.read(buffer))
                    currentTimes++
                }
                if (subFile == chunkSize && fileSize == chunkSize * MAX_LIMIT_SIZE) {
                    subZipFile.renameTo(zipFile)
                }
                filesPath.add(subZipFile.absolutePath)
                bufferedOutputStream.close()
            }
            // handle the remain part of file with names.zip
            var remainZipFile: File? = null
            if (chunkSize > 0 && fileSize > chunkSize * MAX_LIMIT_SIZE) {
                remainZipFile = File(downloadDirectory, "${zipFile.name}.1")
                BufferedOutputStream(FileOutputStream(remainZipFile)).use {
                    bufferedInputStream.copyTo(it, BUFFER_SIZE)
                }
            }
            bufferedInputStream.close()
            remainZipFile?.renameTo(zipFile)
            filesPath.add(zipFile.absolutePath)
            PIMSLogger.i("Finish to split")
        } catch (e: IOException) {
            PIMSLogger.i("Failed to split $zipFile, cause :$e")
        }
        return filesPath
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun zip(file: File, zipFile: File) {
        ZipOutputStream(BufferedOutputStream(FileOutputStream(zipFile))).use { output ->
            BufferedInputStream(FileInputStream(file)).use { origin ->
                val entry = ZipEntry(file.name)
                output.putNextEntry(entry)
                origin.copyTo(output, BUFFER_SIZE)
            }
        }
    }

    @Suppress("SwallowedException")
    fun toBase64(path: String, context: Context): String? {
        var byteArray = getFileByteWithUri(Uri.parse(path), context)
        if (byteArray.isEmpty()) {
            try {
                byteArray = File(path).readBytes()
            } catch (e: FileNotFoundException) {
                PIMSLogger.i("Invalid File here")
            }
        }
        return byteArray.getBase64()
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    @Suppress("TooGenericExceptionCaught", "SwallowedException", "NestedBlockDepth")
    internal fun copy(uri: Uri, context: Context, fileName: String, downloadDirectory: File?) {
        try {
            context.contentResolver.openFileDescriptor(uri, "r")
                ?.use { parcelFileDescriptor ->
                    val zipFile = File(downloadDirectory, "$fileName.zip")
                    BufferedOutputStream(FileOutputStream(zipFile)).use { output ->
                        BufferedInputStream(FileInputStream(parcelFileDescriptor.fileDescriptor)).use { input ->
                            input.copyTo(output)
                        }
                    }
                }
        } catch (e: Exception) {
            PIMSLogger.d("Failed to copy file to download directory: $e")
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    @Suppress("TooGenericExceptionCaught", "SwallowedException", "NestedBlockDepth")
    internal fun zip(context: Context, uri: Uri, zipFile: File, fileName: String) {
        try {
            context.contentResolver.openFileDescriptor(uri, "r")
                ?.use { parcelFileDescriptor ->
                    ZipOutputStream(BufferedOutputStream(FileOutputStream(zipFile)))
                        .use { output ->
                            BufferedInputStream(FileInputStream(parcelFileDescriptor.fileDescriptor))
                                .use { origin ->
                                    val entry = ZipEntry(fileName)
                                    output.putNextEntry(entry)
                                    origin.copyTo(output)
                                }
                        }
                }
        } catch (e: Exception) {
            PIMSLogger.d("failed to zip file: $fileName, $uri , $e")
            // ignore
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    internal fun getFileByteWithUri(uri: Uri, context: Context): ByteArray = try {
        context.contentResolver.openInputStream(uri)
            ?.use { it.readBytes() } ?: ByteArray(0)
    } catch (e: Exception) {
        PIMSLogger.i("read File byte exception")
        ByteArray(0)
    }
}
