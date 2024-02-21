package com.mobiledrivetech.external.common.fundamental.extensions

import android.content.pm.PackageManager.NameNotFoundException
import androidx.lifecycle.SavedStateHandle

fun <T> SavedStateHandle.requireArgument(key: String): T =
    get<T>(key) ?: throw NameNotFoundException("The $key argument is required!")
