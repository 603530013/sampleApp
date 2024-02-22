package com.mobiledrivetech.external.sample.api

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
class Api(
    val name: String,
    val getInput: @RawValue List<Input>? = null,
    val setInput: @RawValue List<Input>? = null
) : Parcelable
