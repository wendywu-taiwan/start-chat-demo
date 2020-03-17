package com.example.wendywu.startchatdemo.utils

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.wendywu.startchatdemo.R
import com.example.wendywu.startchatdemo.data.ReceiverItem

class ReceiverIconView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var userImageIcon: ImageView
    private var userImageTextLayout: ConstraintLayout
    private var userImageText: TextView
    private var mailIcon: ImageView

    init {
        LayoutInflater.from(context).inflate(R.layout.receiver_icon_view, this, true)
        userImageIcon = findViewById<View>(R.id.user_image_icon) as ImageView
        userImageTextLayout = findViewById<View>(R.id.user_image_text_layout) as ConstraintLayout
        userImageText = findViewById<View>(R.id.user_image_text) as TextView
        mailIcon = findViewById<View>(R.id.mail_address_icon) as ImageView
    }

    fun setReceiver(receiver: ReceiverItem) {
        if (receiver.showUserIcon()) {
            userImageIcon.setImageResource(context.drawableIdByName(receiver.image))
            hideView(userImageTextLayout)
            showView(userImageIcon)
        } else if (receiver.showEmailIcon()) {
            val color = ContextCompat.getColor(context, R.color.receiverMailIconBackgroundBlue)
            userImageTextLayout.setBackgroundColor(color)
            showView(mailIcon, userImageTextLayout)
            hideView(userImageText, userImageIcon)
        } else {
            val allColors = context.resources.obtainTypedArray(R.array.receiverTextBackgroundColor)
            val remainder = receiver.id % allColors.length()
            val color: Int = allColors.getColor(remainder, 0)
            allColors.recycle()

            userImageText.text = receiver.getFirstNameChar()
            userImageText.setTextColor(color)
            userImageTextLayout.setBackgroundColor(color)
            userImageTextLayout.background.alpha = 70

            showView(userImageTextLayout)
            hideView(userImageIcon)
        }
    }

    fun setSmallIconText(smallSize: Boolean) {
        val textSize: Float = if (smallSize) context.resources.getDimension(R.dimen.text_size_smallest) else context.resources.getDimension(R.dimen.text_size_large)
        userImageText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }
}