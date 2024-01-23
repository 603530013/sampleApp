package com.mobiledrivetech.external.sample.api

import android.os.Parcelable
import com.mobiledrivetech.external.sample.providers.FacadeDataProvider
import kotlinx.android.parcel.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
sealed class Input(open val name: String) : Parcelable {

    @Parcelize
    class MapInput(
        override val name: String,
        val method: FacadeDataProvider.Method,
        val parameter: @RawValue Map<String, Any>?
    ) : Input(name)

}
