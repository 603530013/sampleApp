package com.mobiledrivetech.external.common.base.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer

fun <T> NonNullMediatorLiveData<T>.observe(owner: LifecycleOwner, observer: (t: T) -> Unit) =
    observe(owner, Observer { it?.let(observer) })

fun <T> LiveData<T>.nonNull(): NonNullMediatorLiveData<T> = NonNullMediatorLiveData<T>().also { mediator ->
    mediator.addSource(this) { it?.let { mediator.value = it } }
}

class NonNullMediatorLiveData<T> : MediatorLiveData<T>()
