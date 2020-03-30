package com.apps.chatychaty

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.core.content.edit
import com.google.android.material.snackbar.Snackbar

fun View.snackbar(value: String) {
    Snackbar.make(this, value, Snackbar.LENGTH_LONG).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
        .show()
}

fun Activity.setPref(key: String, value: String) {
    this.getPreferences(Context.MODE_PRIVATE).edit {
        this.putString(key, value)
        apply()
    }
}

fun Activity.getPref(key: String): String? {
    return this.getPreferences(Context.MODE_PRIVATE).getString(key, null)
}
