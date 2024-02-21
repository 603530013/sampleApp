package com.mobiledrivetech.external.common.fundamental.extensions

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment

fun Fragment.handleFragmentResult(keys: List<String>, onResult: (String, Bundle) -> Unit) {
    keys.forEach { key ->
        val listener: (requestKey: String, bundle: Bundle) -> Unit = { requestKey, bundle ->
            Log.i("FragmentExtensions", "handleFragmentResult | listener | ${javaClass.name} | key: $requestKey | bundle: $bundle")

            onResult(key, bundle)
            requireActivity().supportFragmentManager.clearFragmentResult(requestKey)
        }
        requireActivity().supportFragmentManager.setFragmentResultListener(key, viewLifecycleOwner, listener)
    }
}

fun Fragment.handleClearFragmentResult(keys: List<String>) {
    keys.forEach { key ->
        requireActivity().supportFragmentManager.clearFragmentResultListener(key)
    }
}

fun Fragment.addFragmentResult(key: String, bundle: Bundle) {
    requireActivity().supportFragmentManager.setFragmentResult(key, bundle)
}
