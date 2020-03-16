package com.example.wendywu.startchatdemo.adapter

import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.Color
import android.support.constraint.ConstraintLayout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.wendywu.startchatdemo.R
import com.example.wendywu.startchatdemo.data.ReceiverItem
import com.example.wendywu.startchatdemo.utils.drawableIdByName
import com.example.wendywu.startchatdemo.utils.hideView
import com.example.wendywu.startchatdemo.utils.showView


class ReceiverListAdapter(items: List<ReceiverItem>, ctx: Context) :
        ArrayAdapter<ReceiverItem>(ctx, R.layout.receiver_list_item, items) {

    private class ReceiverListViewHolder {
        internal var image: ImageView? = null
        internal var name: TextView? = null
        internal var email: TextView? = null
        internal var userImageTextLayout: ConstraintLayout? = null
        internal var userImageText: TextView? = null
    }

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View? {
        var view = view
        val viewHolder: ReceiverListViewHolder

        if (view == null) {
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.receiver_list_item, viewGroup, false)

            viewHolder = ReceiverListViewHolder()
            viewHolder.image = view.findViewById<View>(R.id.user_image_icon) as ImageView
            viewHolder.name = view.findViewById<View>(R.id.user_name_text) as TextView
            viewHolder.email = view.findViewById<View>(R.id.user_mail_address_text) as TextView
            viewHolder.userImageTextLayout = view.findViewById<View>(R.id.user_image_text_layout) as ConstraintLayout
            viewHolder.userImageText = view.findViewById<View>(R.id.user_image_text) as TextView
        } else {
            viewHolder = view.tag as ReceiverListViewHolder
        }

        val receiver = getItem(i) ?: return view

        viewHolder.name!!.text = receiver.getName()
        viewHolder.email!!.text = receiver.mailAddress

        if (receiver.showUserIcon()) {
            viewHolder.image!!.setImageResource(context.drawableIdByName(receiver.image))
            viewHolder.userImageTextLayout?.let { hideView(it) }
            viewHolder.image?.let { showView(it) }
        } else {
            val allColors = context.resources.obtainTypedArray(R.array.receiverTextBackgroundColor)
            val remainder = i % allColors.length()
            val color: Int = allColors.getColor(remainder, 0)

            viewHolder.userImageText?.text = receiver.getFirstNameChar()
            viewHolder.userImageText?.setTextColor(color)
            viewHolder.userImageTextLayout?.setBackgroundColor(color)
            viewHolder.userImageTextLayout?.background?.alpha = 70;

            viewHolder.userImageTextLayout?.let { showView(it) }
            viewHolder.image?.let { hideView(it) }
            allColors.recycle()

        }
        if (view != null) {
            view.tag = viewHolder
        }
        return view
    }
}


