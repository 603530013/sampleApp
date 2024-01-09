package com.mobiledrivetech.external.common.base.coordinator

import android.content.ComponentCallbacks
import androidx.annotation.RestrictTo
import androidx.lifecycle.LifecycleOwner
import org.koin.android.ext.android.getKoin
import org.koin.core.component.KoinComponent
import org.koin.core.context.GlobalContext
import org.koin.core.definition.BeanDefinition
import org.koin.core.definition.Definition
import org.koin.core.module.Module
import org.koin.core.qualifier.Qualifier

/**
 * [Coordinator] DSL extension to declare a [Coordinator] in a Koin [Module].
 */
inline fun <reified C> Module.coordinator(
    qualifier: Qualifier? = null,
    override: Boolean = false,
    createdAtStart: Boolean = true,
    noinline definition: Definition<C>
): BeanDefinition<C> where C : Coordinator<*, *> = single(qualifier, createdAtStart, override, definition)

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
fun LifecycleOwner.getKoin() = when (this) {
    is KoinComponent -> this.getKoin()
    is ComponentCallbacks -> (this as ComponentCallbacks).getKoin()
    else -> GlobalContext.get()
}
