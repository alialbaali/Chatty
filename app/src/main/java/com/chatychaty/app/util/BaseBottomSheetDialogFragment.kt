package com.chatychaty.app.util

import com.chatychaty.app.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

open class BaseBottomSheetDialogFragment : BottomSheetDialogFragment() {

    override fun getTheme(): Int = R.style.BottomSheetDialog

}