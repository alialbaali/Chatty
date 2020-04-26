package com.apps.chatychaty.util

import android.content.Context
import android.content.SharedPreferences
import android.view.View
import androidx.core.content.edit
import com.apps.chatychaty.di.SHARED_PREFERENCES_NAME
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineExceptionHandler
import retrofit2.HttpException
import java.net.UnknownHostException

fun View.snackbar(value: String) {
    Snackbar.make(this, value, Snackbar.LENGTH_LONG).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
        .show()
}

fun Context.setPref(key: String, value: String?) {
    this.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit {
        if (value != null) {
            this.putString(key, value)
        }
        apply()
    }
}

fun Context.getPref(key: String): String? {
    return this.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).getString(key, null)
}

object ExceptionHandler {

    val handler = CoroutineExceptionHandler { _, throwable ->
        if (throwable is UnknownHostException) {
//            error.snackbar("Please connect to the internet")
        }
        if (throwable is HttpException) {
//            error.snackbar(throwable.message().toString())
//            val errorResponse =
//                ErrorConverter.converter.convert(throwable.response()?.errorBody()!!)
//            error.snackbar()
        }
    }
}