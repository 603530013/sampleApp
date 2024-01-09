package com.mobiledrivetech.external.common.base.tools

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.mobiledrivetech.external.common.base.extensions.startSafeActivity

object PhoneCallUtil {

    fun startCall(phoneNumber: String, context: Context) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            data = Uri.parse("tel:$phoneNumber")
        }
        context.startSafeActivity(intent) {
            ActivityErrorUtil.showActivityNotFoundError("Not found call activity")
        }
    }
}
