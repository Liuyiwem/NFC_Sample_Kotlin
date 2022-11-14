package com.example.nfc_sample_kotlin

import android.util.Log
import android.view.View


inline val <reified T> T.TAG: String
    get() = T::class.java.simpleName
inline fun <reified T> T.logv(message: String) = Log.v(TAG, message)
inline fun <reified T> T.logi(message: String) = Log.i(TAG, message)
inline fun <reified T> T.logw(message: String) = Log.w(TAG, message)
inline fun <reified T> T.logd(message: String) = Log.d(TAG, message)
inline fun <reified T> T.loge(message: String) = Log.e(TAG, message)

inline fun ByteArray.toHexString() = asUByteArray().joinToString("") { it.toString(16).padStart(2, '0') }
inline fun View.hide() = run { visibility = View.INVISIBLE }
inline fun View.show() = run { visibility = View.VISIBLE }
