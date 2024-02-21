package com.mobiledrivetech.external.middleware.extensions

import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PackageInfoFlags
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES

internal fun Context.applicationName(): String =
    packageManager.getApplicationLabel(applicationInfo).toString()

internal fun Context.applicationVersion(): String = packageManager.run {
    val packageName = this@applicationVersion.packageName
    if (VERSION.SDK_INT >= VERSION_CODES.TIRAMISU) {
        getPackageInfo(packageName, PackageInfoFlags.of(PackageManager.GET_ACTIVITIES.toLong()))
    } else {
        @Suppress("deprecation")
        getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
    }
}.versionName
