package com.example.wendywu.startchatdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import android.view.View.OnFocusChangeListener
import com.example.wendywu.startchatdemo.adapter.ReceiverAutoCompleteAdapter
import com.example.wendywu.startchatdemo.data.ReceiverItem
import com.example.wendywu.startchatdemo.utils.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val LIMIT_SUBJECT_WARNING_COUNT = 80
const val LIMIT_SUBJECT_LIMIT_COUNT = 85
const val LIMIT_MESSAGE_WARNING_COUNT = 4950
const val LIMIT_MESSAGE_LIMIT_COUNT = 5000


class MainActivity : AppCompatActivity() {

    private var messageLayoutOriginHeight = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setAdapter()
        setListener()
    }

    private fun setAdapter() {
        val jsonFileString = getJsonDataFromAsset(applicationContext, "dummy_data.json")
        Log.d("setAdapter", "jsonFileString:" + jsonFileString)

        val gson = Gson()
        val receiverType = object : TypeToken<List<ReceiverItem>>() {}.type
        var receivers: List<ReceiverItem> = gson.fromJson(jsonFileString, receiverType)

//        val listPersonType = object : TypeToken<List<ReceiverItem>>() {}.type
//        var receivers: List<ReceiverItem> = gson.fromJson(jsonFileString, listPersonType)
        val adapter = ReceiverAutoCompleteAdapter(receivers, baseContext)
        receiver_auto_complete_text.threshold = 1
        receiver_auto_complete_text.setAdapter(adapter)
    }


    private fun setListener() {
        message_expand_icon.setOnClickListener {
            expandMessageView()
            hideView(message_expand_icon)
        }


        subject_edit_text.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(s: Editable) {
                // check subject text count and modify view visibility
                val length = subject_edit_text.text.toString().length

                if (length > 0) {
                    hideView(subject_title_text, subject_hint_text)
                    showView(subject_title_hint_text)
                } else {
                    hideView(subject_title_hint_text)
                    showView(subject_title_text, subject_hint_text)
                }
                // show the subject character count
                subject_count_text.text = combineText(length.toString(), "/", LIMIT_SUBJECT_LIMIT_COUNT.toString())
                // change count text to red if the count close to the limited size
                if (length > LIMIT_SUBJECT_WARNING_COUNT) {
                    subject_count_text.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorTextCountWarning))
                } else {
                    subject_count_text.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorTextViewTitle))
                }
            }
        })

        message_edit_text.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(s: Editable) {
                val length = message_edit_text.text.toString().length
                message_count_text.text = combineText(length.toString(), "/", LIMIT_MESSAGE_LIMIT_COUNT.toString())
                if (length > LIMIT_MESSAGE_WARNING_COUNT) showView(message_count_text) else hideView(message_count_text)
            }
        })

        subject_edit_text.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) hideKeyboard(message_layout)
        }

        message_edit_text.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                setBackgroundDrawable(message_layout, R.drawable.message_input_type_shape)
            } else {
                setBackgroundDrawable(message_layout, R.drawable.message_input_shape)
                collapseMessageView()
                hideKeyboard(message_layout)
                showView(message_expand_icon)
            }
        }
    }

    private fun expandMessageView() {
        messageLayoutOriginHeight = message_layout.height
        message_layout.layoutParams.height = ConstraintLayout.LayoutParams.MATCH_PARENT
        message_layout.requestLayout()
    }

    private fun collapseMessageView() {
        if (messageLayoutOriginHeight == 0 || message_layout.height <= messageLayoutOriginHeight)
            return

        message_layout.layoutParams.height = messageLayoutOriginHeight
        message_layout.requestLayout()
    }
}
