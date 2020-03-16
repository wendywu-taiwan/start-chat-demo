package com.example.wendywu.startchatdemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.wendywu.startchatdemo.R
import com.example.wendywu.startchatdemo.data.ReceiverItem
import com.example.wendywu.startchatdemo.utils.drawableIdByName


class ReceiverAutoCompleteAdapter(items: List<ReceiverItem>, ctx: Context) :
        ArrayAdapter<ReceiverItem>(ctx, R.layout.receiver_list_item, items) {

    private class ReceiverAutoCompleteItemViewHolder {
        internal var image: ImageView? = null
        internal var name: TextView? = null
        internal var email: TextView? = null
    }

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View {
        var view = view

        val viewHolder: ReceiverAutoCompleteItemViewHolder

        if (view == null) {
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.receiver_list_item, viewGroup, false)

            viewHolder = ReceiverAutoCompleteItemViewHolder()
            viewHolder.image = view!!.findViewById<View>(R.id.user_image_icon) as ImageView
            viewHolder.name = view.findViewById<View>(R.id.user_name_text) as TextView
            viewHolder.email = view.findViewById<View>(R.id.user_mail_address_text) as TextView
        } else {
            //no need to call findViewById, can use existing ones from saved view holder
            viewHolder = view.tag as ReceiverAutoCompleteItemViewHolder
        }

        R.drawable.momday_dok

        val receiver = getItem(i)
        viewHolder.name!!.text = receiver.getName()
        viewHolder.email!!.text = receiver.mailAddress
        viewHolder.image!!.setImageResource(context.drawableIdByName(receiver.image))
        view.tag = viewHolder
        return view
    }
}

