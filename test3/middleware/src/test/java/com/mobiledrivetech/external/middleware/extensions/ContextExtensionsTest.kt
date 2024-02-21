package com.mobiledrivetech.external.middleware.extensions

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert
import org.junit.Test

class ContextExtensionsTest {
    private val context: Context = mockk(relaxed = true)
    private val packageManager: PackageManager = mockk(relaxed = true)

    @Test
    fun `fun applicationName from getApplicationLabel`() {
        val name = "testName"
        val applicationInfo: ApplicationInfo = mockk()
        every { context.packageManager } returns packageManager
        every { context.applicationInfo } returns applicationInfo
        every { context.packageManager.getApplicationLabel(any()) } returns name
        val result = context.applicationName()
        Assert.assertEquals(name, result)
        verify { context.packageManager.getApplicationLabel(applicationInfo) }
    }

    @Test
    fun `fun applicationVersion then get version info`() {
        val packageInfo: PackageInfo = mockk()
        val versionName = "versionName"
        packageInfo.versionName = versionName
        val packageName = "testPackageName"
        every { context.packageName } returns packageName
        every {
            context.packageManager.getPackageInfo(
                any<String>(),
                any<Int>()
            )
        } returns packageInfo
        val version = context.applicationVersion()
        Assert.assertEquals(versionName, version)
        verify { context.packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES) }
    }

}