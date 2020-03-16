package com.example.wendywu.startchatdemo.utils

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.util.AttributeSet


class RoundImageView : android.support.v7.widget.AppCompatImageView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
            context,
            attrs,
            defStyleAttr
    )

    override fun setImageDrawable(drawable: Drawable?) {
        if (drawable == null)
            return
        super.setImageDrawable(drawable)
        val radius = 0.1f
        val bitmap = (drawable as BitmapDrawable).bitmap
        val resourceId = RoundedBitmapDrawableFactory.create(resources, bitmap)
        resourceId.cornerRadius = bitmap.width * radius
        super.setImageDrawable(resourceId)
    }
}