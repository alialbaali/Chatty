package com.chatychaty.app.util

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chatychaty.app.R
import com.chatychaty.di.SHARED_PREFERENCES_NAME
import com.google.android.material.snackbar.Snackbar

fun View.snackbar(value: String) = Snackbar.make(this, value, Snackbar.LENGTH_LONG)
    .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
    .show()


fun View.toast(value: String): Toast = Toast.makeText(context, value, Toast.LENGTH_LONG).apply {
    view.findViewById<TextView>(android.R.id.message).apply {
        typeface = ResourcesCompat.getFont(context, R.font.roboto)
        setTextColor(resources.getColor(R.color.colorAccent))
    }
    view.background.colorFilter = PorterDuffColorFilter(resources.getColor(R.color.colorSurface), PorterDuff.Mode.SRC_IN)
    show()
}

fun Context.getPref(key: String): String? {
    return this.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).getString(key, null)
}

fun <T> MutableLiveData<T>.asLiveData(): LiveData<T> = this