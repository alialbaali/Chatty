package com.alialbaali.chatychaty.util

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.alialbaali.chatychaty.R
import com.bumptech.glide.Glide

@BindingAdapter("app:imageUrl")
fun ImageView.setImage(imageUrl: String?) {
    Glide.with(context)
        .load(imageUrl)
        .placeholder(context.getDrawable(R.drawable.ic_person_24dp))
        .circleCrop()
        .into(this)
}

@BindingAdapter("app:isMessageDelivered")
fun ImageView.isMessageDelivered(isDelivered: Boolean?) {
    if (isDelivered == true){
        this.setImageResource(R.drawable.ic_done_24dp)
    } else {
        this.setImageResource(R.drawable.ic_loading_24dp)
    }
}