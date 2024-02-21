package com.mobiledrivetech.external.common.base.extensions

import androidx.lifecycle.ViewModel
import org.koin.core.context.GlobalContext

inline fun <reified T : Any> ViewModel.kInject(): Lazy<T> = GlobalContext.get().inject()

inline fun <reified T : Any> kInject(): Lazy<T> = GlobalContext.get().inject()

inline fun <reified T : Any> kGet(): T = GlobalContext.get().get()

inline fun <reified T : Any> kGetOrNull(): T? = GlobalContext.getOrNull()?.getOrNull()
