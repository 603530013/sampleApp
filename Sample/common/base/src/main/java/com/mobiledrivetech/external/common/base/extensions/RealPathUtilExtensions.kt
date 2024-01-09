package com.mobiledrivetech.external.common.base.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.mobiledrivetech.external.common.base.tools.RealPathUtil

fun Intent?.getFilePath(context: Context): String =
    this?.data?.let { data -> RealPathUtil.getRealPath(context, data) ?: "" } ?: ""

fun Uri?.getFilePath(context: Context): String = this?.let { uri -> RealPathUtil.getRealPath(context, uri) ?: "" } ?: ""
