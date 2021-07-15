package com.chatychaty.app.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.chatychaty.app.R

@BindingAdapter("app:imageUrl")
fun ImageView.setImage(imageUrl: String?) = Glide.with(context)
    .load(imageUrl)
    .placeholder(context.getDrawable(R.drawable.ic_round_person_24))
    .into(this)