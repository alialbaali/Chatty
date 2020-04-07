package com.apps.chatychaty

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.core.content.edit
import com.apps.chatychaty.viewModel.Error
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineExceptionHandler
import retrofit2.HttpException
import java.net.UnknownHostException

fun View.snackbar(value: String) {
    Snackbar.make(this, value, Snackbar.LENGTH_LONG).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
        .show()
}

fun Activity.setPref(key: String, value: String?) {
    this.getPreferences(Context.MODE_PRIVATE).edit {
        if (value != null) {
            this.putString(key, value)
        }
        apply()
    }
}

fun Activity.getPref(key: String): String? {
    return this.getPreferences(Context.MODE_PRIVATE).getString(key, null)
}

internal object ExceptionHandler {
    lateinit var error: Error

    val handler = CoroutineExceptionHandler { _, throwable ->
        if (throwable is UnknownHostException) {
        error.snackbar("Please connect to the internet")
        }
        if (throwable is HttpException) {
            error.snackbar(throwable.message().toString())
//            val errorResponse =
//                ErrorConverter.converter.convert(throwable.response()?.errorBody()!!)
//            error.snackbar()
        }
    }
}