package com.example.wendywu.startchatdemo.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.wendywu.startchatdemo.R
import com.example.wendywu.startchatdemo.data.ReceiverItem
import com.example.wendywu.startchatdemo.utils.ReceiverIconView
import java.util.*
import kotlin.collections.ArrayList


class ReceiverListAdapter(items: List<ReceiverItem>, ctx: Context) :
        ArrayAdapter<ReceiverItem>(ctx, R.layout.receiver_list_item, items), Filterable {

    private var receivers: List<ReceiverItem> = items
    private var allReceivers: List<ReceiverItem> = items
    private var selectedReceivers: ArrayList<ReceiverItem> = ArrayList<ReceiverItem>()

    private class ReceiverListViewHolder {
        internal var name: TextView? = null
        internal var email: TextView? = null
        internal var receiverIconView: ReceiverIconView? = null
    }

    override fun getCount(): Int {
        return receivers.size
    }

    override fun getItem(p0: Int): ReceiverItem? {
        return receivers[p0]
    }

    override fun getItemId(p0: Int): Long {
        // Or just return p0
        return receivers[p0].id.toLong()
    }

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View? {
        var view = view
        val viewHolder: ReceiverListViewHolder

        if (view == null) {
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.receiver_list_item, viewGroup, false)

            viewHolder = ReceiverListViewHolder()
            viewHolder.name = view.findViewById<View>(R.id.user_name_text) as TextView
            viewHolder.email = view.findViewById<View>(R.id.user_mail_address_text) as TextView
            viewHolder.receiverIconView = view.findViewById<View>(R.id.receiver_icon_view) as ReceiverIconView
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ReceiverListViewHolder
        }

        val receiver = getItem(i) ?: return view
        viewHolder.name!!.text = receiver.getName()
        viewHolder.email!!.text = receiver.mailAddress
        viewHolder.receiverIconView!!.setReceiver(receiver)

        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(constraint: kotlin.CharSequence?, results: FilterResults) {
                receivers = results.values as List<ReceiverItem>
                notifyDataSetChanged()
            }

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val queryString = constraint?.toString()?.toLowerCase(Locale.ROOT)
                val filterResults = FilterResults()
                filterResults.values = if (queryString == null || queryString.isEmpty())
                    allReceivers
                else
                    allReceivers.filter {
                        (it.lastName.toLowerCase().contains(queryString) ||
                                it.firstName.toLowerCase().contains(queryString) ||
                                it.mailAddress.toLowerCase().contains(queryString)) && !selectedReceivers.contains(it)
                    }
                return filterResults
            }
        }
    }

    fun hasSelectedItem(): Boolean {
        Log.d("check hasSelectedItem","size="+selectedReceivers.size)
        return selectedReceivers.size != 0
    }

    fun addSelectItem(selectedItem: ReceiverItem) {
        selectedReceivers.add(selectedItem)
    }

    fun removeSelectItem(id: Int) {
        for (receiver in allReceivers) {
            if (receiver.id == id) {
                selectedReceivers.remove(receiver)
                break
            }
        }
    }
}