package com.mobiledrivetech.external.common.base.extensions

import android.util.Log


fun resetField(target: Any, fieldName: String) {
    try {
        val targetClass = target.javaClass
        val fields = targetClass.declaredFields().filter { it.name.contains(fieldName, true) }

        if (fields.isNullOrEmpty()) {
            throw IllegalArgumentException("Field $fieldName was not found on ${target.javaClass.name}")
        }

        fields.forEach { field ->
            field.isAccessible = true
            field.set(target, null)
        }
    } catch (ex: Exception) {
        Log.w("FieldsExtensions","$ex, resetField")
    }
}

private fun Class<Any>.declaredFields() = (
    declaredFields +
        superclass.declaredFields +
        superclass.superclass.declaredFields +
        superclass.superclass.superclass.declaredFields
    ).toHashSet()
