package com.example.wendywu.startchatdemo.utils

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.wendywu.startchatdemo.R
import com.example.wendywu.startchatdemo.data.ReceiverItem

class SelectedReceiverItemView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var removeSelectedReceiverIcon: ImageView? = null
    private var selectedReceiverName: TextView? = null
    private var receiverIconView: ReceiverIconView? = null

    init {
        val view: View = LayoutInflater.from(context).inflate(R.layout.selected_receiver_item_view, this, true)
        var params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        params.setMargins(0, 0, 20, 20)
        view.layoutParams = params

        removeSelectedReceiverIcon = findViewById<View>(R.id.remove_selected_receiver_icon) as ImageView
        selectedReceiverName = findViewById<View>(R.id.selected_receiver_text) as TextView
        receiverIconView = findViewById<View>(R.id.receiver_icon_view) as ReceiverIconView
    }

    fun setReceiver(receiver: ReceiverItem, removeSelectedReceiver: (Int) -> Unit) {
        receiverIconView!!.setReceiver(receiver)
        receiverIconView?.setSmallIconText(true)
        selectedReceiverName?.text = receiver.getSelectedReceiverDisplayName()
        removeSelectedReceiverIcon!!.setOnClickListener {
            removeSelectedReceiver(receiver.id)
        }
    }
}