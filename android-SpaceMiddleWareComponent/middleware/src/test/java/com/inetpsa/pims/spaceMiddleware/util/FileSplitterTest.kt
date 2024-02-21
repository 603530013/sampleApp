package com.inetpsa.pims.spaceMiddleware.util

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.inetpsa.mmx.foundation.monitoring.logger.PIMSLogger
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException

class FileSplitterTest {

    private companion object {

        const val TEST_KB = 1024
        const val TEST_LARGE_FILE = 11 * TEST_KB * TEST_KB
    }

    private val context: Context = mockk(relaxed = true)
    private val splitter: FileSplitter = spyk()
    private val uri: Uri = mockk()

    @Before
    fun setup() {
        mockkStatic("kotlin.io.FilesKt")
        mockkStatic(Uri::parse)
        every { Uri.parse(any()) } returns uri
    }

    @After
    fun release() {
        unmockkAll()
        clearAllMocks()
    }

    @Test
    fun `when handleUploadFile and size less than MAX_LIMIT_SIZE then return its path as list`() {
        val byteArray = ByteArray(TEST_KB)
        val fileName = "test_file.txt"
        every { splitter.getFileByteWithUri(any(), any()) } returns byteArray
        every { uri.toString() } returns "test_path.txt"

        val actual = splitter.handleUploadFile("test_path", fileName, context)
        Assert.assertEquals(1, actual.size)
        Assert.assertTrue(actual[0].endsWith("test_path.txt"))
    }

    @Test
    fun `when handleUploadFile, need to zip it and zipFile less than MAX_LIMIT_SIZE return zipFile path as list`() {
        val byteArray = ByteArray(TEST_LARGE_FILE)
        val downloadDirectory = File("testDirectory")
        downloadDirectory.mkdir()
        val zipFile = File(downloadDirectory, "test_path.zip")
        zipFile.createNewFile()
        every { splitter.getFileByteWithUri(any(), any()) } returns byteArray
        every { context.getExternalFilesDir(any()) } returns downloadDirectory
        justRun { splitter.zip(any(), any(), any(), any()) }
        zipFile.writeBytes(ByteArray(10))

        val actual = splitter.handleUploadFile("testPath", "test_path.txt", context)
        verify { splitter.zip(context, uri, zipFile, "test_path") }
        Assert.assertEquals(1, actual.size)
        Assert.assertEquals(true, actual[0].endsWith("testDirectory${File.separator}test_path.zip"))
        zipFile.deleteOnExit()
        downloadDirectory.deleteOnExit()
    }

    @Test
    fun `when handleUploadFile and after zip still over MAX_LIMIT_SIZE then split it and return children paths`() {
        val byteArray = ByteArray(TEST_LARGE_FILE)
        val childrenPaths = listOf("test_path_1.z01", "test_path.zip")
        val downloadDirectory = File("testDirectory")
        downloadDirectory.mkdir()
        val zipFile = File(downloadDirectory, "test_path.zip")
        zipFile.createNewFile()
        zipFile.writeBytes(byteArray)
        every { splitter.getFileByteWithUri(any(), any()) } returns byteArray
        every { context.getExternalFilesDir(any()) } returns downloadDirectory
        every { splitter.split(any(), any()) } returns childrenPaths
        justRun { splitter.zip(any(), any(), any(), any()) }

        val actual = splitter.handleUploadFile("test_path", "test_path.txt", context)
        verify { splitter.zip(context, uri, zipFile, "test_path") }
        verify { splitter.split(zipFile, downloadDirectory) }
        Assert.assertEquals(childrenPaths.size, actual.size)
        childrenPaths.forEachIndexed { index, expectedValue ->
            Assert.assertEquals(expectedValue, actual[index])
        }
        zipFile.deleteOnExit()
        downloadDirectory.deleteOnExit()
    }

    @Test
    fun `when handleUploadFile, input ZIP file over MAX_LIMIT_SIZE then split it and return its paths list`() {
        val byteArray = ByteArray(TEST_LARGE_FILE)
        val childrenPaths = listOf("test_path.z01", "test_path.zip")
        val downloadDirectory = File("testDirectory")
        downloadDirectory.mkdir()
        val zipFile = File(downloadDirectory, "test_path.zip")
        zipFile.createNewFile()
        every { splitter.getFileByteWithUri(any(), any()) } returns byteArray
        every { context.getExternalFilesDir(any()) } returns downloadDirectory
        every { splitter.split(any(), any()) } returns childrenPaths
        justRun { splitter.copy(any(), any(), any(), any()) }

        val actual = splitter.handleUploadFile("test_path", "test_path.zip", context)
        verify { splitter.copy(uri, context, "test_path", downloadDirectory) }
        verify { splitter.split(zipFile, downloadDirectory) }
        Assert.assertEquals(childrenPaths.size, actual.size)
        childrenPaths.forEachIndexed { index, expectedValue ->
            Assert.assertEquals(expectedValue, actual[index])
        }
        zipFile.deleteOnExit()
        downloadDirectory.deleteOnExit()
    }

    @Test
    fun `when handleUploadImage and invalid Uri then return empty ByteArray`() {
        mockkStatic(ContentResolver::openInputStream)
        mockkStatic(BitmapFactory::class)
        val exception = FileNotFoundException()
        every { context.contentResolver.openInputStream(any()) } throws exception

        val actual = splitter.handleUploadImage("test_uri", 20, context)
        Assert.assertEquals(0, actual.size)
        verify { context.contentResolver.openInputStream(uri) }
        verify(exactly = 0) { BitmapFactory.decodeStream(any()) }
    }

    @Test
    fun `when handleUploadImage and valid Uri then go to compress and return ByteArray`() {
        mockkStatic(BitmapFactory::class)
        val bitmap: Bitmap = mockk()
        val slot = slot<ByteArrayOutputStream>()
        every { BitmapFactory.decodeStream(any()) } returns bitmap
        every { bitmap.compress(any(), any(), capture(slot)) } returns true

        val actual = splitter.handleUploadImage("test_uri", 20, context)
        Assert.assertEquals(slot.captured.toByteArray().size, actual.size)
        verify { context.contentResolver.openInputStream(uri) }
        verify { BitmapFactory.decodeStream(any()) }
        verify { bitmap.compress(any(), any(), any()) }
    }

    @Test
    fun `when split then return children paths`() {
        val byteArray = ByteArray(TEST_LARGE_FILE)
        val zipFile = File("test_path.zip")
        zipFile.createNewFile()
        zipFile.writeBytes(byteArray)
        val childrenPaths = listOf("test_path.z001", "test_path.zip")
        val downloadDirectory = File("testDirectory")
        downloadDirectory.mkdir()
        val subZipFile = File(downloadDirectory, "test_path.z001")
        subZipFile.createNewFile()
        val remainZipFile = File(downloadDirectory, "test_path.zip.1")
        remainZipFile.createNewFile()

        val actual = splitter.split(zipFile, downloadDirectory)
        actual.forEachIndexed { index, s ->
            println("actual: index: $index, path: $s")
        }
        Assert.assertEquals(childrenPaths.size, actual.size)
        childrenPaths.forEachIndexed { index, expectedValue ->
            println("expectedValue: $expectedValue")
            Assert.assertEquals(true, actual[index].endsWith(expectedValue))
        }
        zipFile.deleteOnExit()
        subZipFile.deleteOnExit()
        remainZipFile.deleteOnExit()
        downloadDirectory.deleteOnExit()
    }

    @Test
    fun `when zip then copy files data to zipFile`() {
        val file = File("test_path.txt")
        file.createNewFile()
        file.writeBytes(ByteArray(TEST_KB))
        val zipFile = File("test_path.zip")
        zipFile.createNewFile()

        splitter.zip(file, zipFile)
        Assert.assertTrue(file.length() > zipFile.length())
        file.deleteOnExit()
        zipFile.deleteOnExit()
    }

    @Test
    fun `when toBase64 with valid Uri then go to getFileByteWithUri and return a base64 String`() {
        mockkStatic(ByteArray::getBase64)
        val byteArray = ByteArray(TEST_KB)
        every { splitter.getFileByteWithUri(any(), any()) } returns byteArray
        every { byteArray.getBase64() } returns "test_base_64"

        val actual = splitter.toBase64("test_path.txt", context)
        Assert.assertEquals("test_base_64", actual)
        verify { splitter.getFileByteWithUri(uri, context) }
        verify { byteArray.getBase64() }
    }

    @Test
    fun `when toBase64 with invalid Uri then try its path and return a base64 String`() {
        mockkStatic(ByteArray::getBase64)
        val byteArray = ByteArray(0)
        val fileByte = ByteArray(TEST_KB)
        val file = File("test_path.txt")
        file.createNewFile()
        file.writeBytes(fileByte)
        every { splitter.getFileByteWithUri(any(), any()) } returns byteArray
        every { fileByte.getBase64() } returns "test_base_64"

        val actual = splitter.toBase64("test_path.txt", context)
        Assert.assertEquals("test_base_64", actual)
        verify { splitter.getFileByteWithUri(uri, context) }
        verify { fileByte.getBase64() }
        file.deleteOnExit()
    }

    @Test
    fun `when toBase64 with invalid File then try its path and return null`() {
        mockkStatic(ByteArray::getBase64)
        val byteArray = ByteArray(0)
        every { splitter.getFileByteWithUri(any(), any()) } returns byteArray

        val actual = splitter.toBase64("test_path.txt", context)
        Assert.assertEquals(null, actual)
        verify { splitter.getFileByteWithUri(uri, context) }
    }

    @Test
    fun `when copy zip file then get a similar file under downloadDirectory `() {
        val uri: Uri = mockk()
        val downloadDirectory = File("testDirectory")
        downloadDirectory.mkdir()
        val file = File("test.zip")
        file.createNewFile()
        file.writeBytes(byteArrayOf(1, 2, 3))
        val fd = FileInputStream(file).fd
        every { context.contentResolver.openFileDescriptor(any(), any())?.fileDescriptor } returns fd

        splitter.copy(uri, context, "testFile", downloadDirectory)
        val copiedFile = File(downloadDirectory, "testFile.zip")
        Assert.assertEquals(file.length(), copiedFile.length())
        verify { context.contentResolver.openFileDescriptor(uri, "r") }
        downloadDirectory.deleteOnExit()
        file.deleteOnExit()
    }

    @Test
    fun `when copy with invalid uri then do nothing and just print log info`() {
        mockkStatic(PIMSLogger::class)
        val uri: Uri = mockk()
        val downloadDirectory = File("testDirectory")
        downloadDirectory.mkdir()
        val file = File("test.zip")
        val exception = FileNotFoundException()
        every { context.contentResolver.openFileDescriptor(any(), any()) } throws exception

        splitter.copy(uri, context, "testFile", downloadDirectory)
        PIMSLogger.d("Failed to copy file to download directory: $exception")
        downloadDirectory.deleteOnExit()
        file.deleteOnExit()
    }

    @Test
    fun `when zip with uri then get a smaller zip file under downloadDirectory`() {
        val uri: Uri = mockk()
        val downloadDirectory = File("testDirectory")
        downloadDirectory.mkdir()
        val file = File("test.txt")
        file.createNewFile()
        file.writeBytes(ByteArray(TEST_LARGE_FILE))
        val fd = FileInputStream(file).fd
        val zipFile = File(downloadDirectory, "test.zip")
        every { context.contentResolver.openFileDescriptor(any(), any())?.fileDescriptor } returns fd

        splitter.zip(context, uri, zipFile, "test")
        Assert.assertTrue(file.length() > zipFile.length())
        verify { context.contentResolver.openFileDescriptor(uri, "r") }
        downloadDirectory.deleteOnExit()
        file.deleteOnExit()
    }

    @Test
    fun `when zip with invalid uri then do nothing and just print log info`() {
        mockkStatic(PIMSLogger::class)
        val uri: Uri = mockk()
        val downloadDirectory = File("testDirectory")
        downloadDirectory.mkdir()
        val file = File("test.txt")
        val zipFile = File(downloadDirectory, "test.zip")
        val exception = FileNotFoundException()
        every { context.contentResolver.openFileDescriptor(any(), any()) } throws exception

        splitter.zip(context, uri, zipFile, "test")
        verify { PIMSLogger.d("failed to zip file: test, $uri , $exception") }
        downloadDirectory.deleteOnExit()
        file.deleteOnExit()
    }

    @Test
    fun `when getFileByteWithUri with valid Uri then return it byteArray`() {
        val byteArray = "test data".toByteArray()
        val inputStream = ByteArrayInputStream(byteArray)
        val uri: Uri = mockk()
        every { context.contentResolver.openInputStream(any()) } returns inputStream

        val actual = splitter.getFileByteWithUri(uri, context)
        Assert.assertEquals(byteArray.size, actual.size)
        verify { context.contentResolver.openInputStream(uri) }
    }

    @Test
    fun `when getFileByteWithUri with invalid Uri then return empty byteArray`() {
        val exception = FileNotFoundException()
        val uri: Uri = mockk()
        every { context.contentResolver.openInputStream(any()) } throws exception

        val actual = splitter.getFileByteWithUri(uri, context)
        Assert.assertEquals(0, actual.size)
        verify { context.contentResolver.openInputStream(uri) }
    }
}
