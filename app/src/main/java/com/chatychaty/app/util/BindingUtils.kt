package com.chatychaty.app.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.chatychaty.app.R

@BindingAdapter("app:imageUrl")
fun ImageView.setImage(imageUrl: String?) = Glide.with(context)
    .load(imageUrl)
    .placeholder(context.getDrawable(R.drawable.ic_person_24dp))
    .into(this)

@BindingAdapter("app:isMessageDelivered")
fun ImageView.isMessageDelivered(isDelivered: Boolean?) =
    if (isDelivered == true) setImageResource(R.drawable.ic_done_24dp) else setImageResource(R.drawable.ic_loading_24dp)


//@BindingAdapter("app:onCheck")
//fun RadioButton.onCheck() {
//    setOnClickListener {
//        isChecked = !isChecked
//    }
//    if (isChecked) {
//        text = resources.getString(R.string.unmute)
//        background = resources.getDrawable(R.drawable.volume_off, null)
//    } else {
//        text = resources.getString(R.string.mute)
//        background = resources.getDrawable(R.drawable.volume_high, null)
//    }
//}