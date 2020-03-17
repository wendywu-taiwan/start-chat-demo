package com.example.wendywu.startchatdemo

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.example.wendywu.startchatdemo.adapter.ReceiverListAdapter
import com.example.wendywu.startchatdemo.data.ReceiverItem
import com.example.wendywu.startchatdemo.utils.*
import com.google.android.flexbox.FlexboxLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*


const val LIMIT_SUBJECT_WARNING_COUNT = 80
const val LIMIT_SUBJECT_LIMIT_COUNT = 85
const val LIMIT_MESSAGE_WARNING_COUNT = 4950
const val LIMIT_MESSAGE_LIMIT_COUNT = 5000


class MainActivity : AppCompatActivity() {

    private var receiverLayoutOriginHeight = 0
    private var messageLayoutOriginHeight = 0

    private var adapter: ReceiverListAdapter? = null

    private val selectedReceiverViewMap: MutableMap<Int, View> = mutableMapOf()

    private lateinit var messageLayout: ConstraintLayout
    private lateinit var subjectLayout: ConstraintLayout
    private lateinit var receiverLayout: ConstraintLayout
    private lateinit var addSelectedReceiverScrollLayout: ConstraintLayout
    private lateinit var addSelectedReceiverLayout: FlexboxLayout
    private lateinit var receiverAutoCompleteText: CustomAutoCompleteTextView
    private lateinit var receiverTitleText: TextView
    private lateinit var receiverTitleHintText: TextView
    private lateinit var subjectEditText: EditText
    private lateinit var subjectTitleText: TextView
    private lateinit var subjectHintText: TextView
    private lateinit var subjectTitleHintText: TextView
    private lateinit var subjectCountText: TextView
    private lateinit var messageEditText: EditText
    private lateinit var messageCountText: TextView
    private lateinit var messageExpandIcon: ImageView
    private lateinit var messageSendIcon: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setView()
        setAdapter()
        setListener()
    }

    private fun setView() {
        messageLayout = message_layout
        subjectLayout = subject_layout
        receiverLayout = receiver_layout
        addSelectedReceiverScrollLayout = selected_receiver_scroll_layout
        addSelectedReceiverLayout = selected_receiver_layout
        receiverAutoCompleteText = receiver_auto_complete_text
        receiverTitleText = receiver_title_text
        receiverTitleHintText = receiver_title_hint_text
        subjectEditText = subject_edit_text
        subjectTitleText = subject_title_text
        subjectHintText = subject_hint_text
        subjectTitleHintText = subject_title_hint_text
        subjectCountText = subject_count_text
        messageEditText = message_edit_text
        messageCountText = message_count_text
        messageExpandIcon = message_expand_icon
        messageSendIcon = message_send_icon

    }

    private fun setAdapter() {
        val jsonFileString = getJsonDataFromAsset(applicationContext, "dummy_data.json")
        val gson = Gson()
        val receiverType = object : TypeToken<List<ReceiverItem>>() {}.type
        var receivers: List<ReceiverItem> = gson.fromJson(jsonFileString, receiverType)
        adapter = ReceiverListAdapter(receivers, baseContext)
        receiverAutoCompleteText.threshold = 1
        receiverAutoCompleteText.setAdapter(adapter)
    }


    private fun setListener() {
        messageExpandIcon.setOnClickListener {
            hideView(message_expand_icon)
            expandMessageView()
        }

        receiverAutoCompleteText.onItemClickListener = OnItemClickListener { _, _, position, _ ->
            val receiverItem = adapter!!.getItem(position)
            if (receiverItem != null) {
                addSelectedReceiverItem(receiverItem)
                if (receiverLayoutOriginHeight == 0) receiverLayoutOriginHeight = addSelectedReceiverScrollLayout.height
            }

        }

        receiverAutoCompleteText.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    val mailAddress = receiverAutoCompleteText.text.toString()
                    if (!isEmailValid(mailAddress)) {
                        receiverAutoCompleteText.error = "Please enter correct email";
                    } else {
                        receiverAutoCompleteText.setText("")
                        val currentTimestamp = System.currentTimeMillis()
                        val receiverItem = ReceiverItem(currentTimestamp.toInt(), mailAddress)
                        addSelectedReceiverItem(receiverItem)
                    }
                    return true
                }
                return false
            }
        })

        // set text change listener
        receiverAutoCompleteText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                if (receiverLayoutOriginHeight == 0) receiverLayoutOriginHeight = selected_receiver_scroll_layout.height
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(s: Editable) {
                val length = receiver_auto_complete_text.text.toString().length
                checkShowReceiverTitleView()
            }
        })

        subjectEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(s: Editable) {
                // check subject text count and modify view visibility
                val length = subjectEditText.text.toString().length

                if (length > 0) {
                    showSubjectTitleView(false)
                    hideView(subject_hint_text)
                } else {
                    showSubjectTitleView(true)
                    showView(subject_hint_text)
                }
                // show the subject character count
                subjectCountText.text = combineText(length.toString(), "/", LIMIT_SUBJECT_LIMIT_COUNT.toString())
                // change count text to red if the count close to the limited size
                if (length > LIMIT_SUBJECT_WARNING_COUNT)
                    subjectCountText.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorTextCountWarning))
                else
                    subjectCountText.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorTextViewTitle))

                checkMessageSend()
            }
        })

        messageEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(s: Editable) {
                val length = message_edit_text.text.toString().length
                message_count_text.text = combineText(length.toString(), "/", LIMIT_MESSAGE_LIMIT_COUNT.toString())
                if (length > LIMIT_MESSAGE_WARNING_COUNT) showView(message_count_text) else hideView(message_count_text)
                checkMessageSend()
            }
        })

        // set on focus listener
        receiverAutoCompleteText.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                setBackgroundDrawable(receiverLayout, R.drawable.text_input_background)
                hideKeyboard(message_layout)
                addSelectedReceiverScrollLayout.layoutParams.height = receiverLayoutOriginHeight
            } else {
                setBackgroundDrawable(receiverLayout, R.drawable.text_input_focus_background)
                addSelectedReceiverScrollLayout.layoutParams.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
            }
            addSelectedReceiverScrollLayout.requestLayout()

        }

        subjectEditText.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                setBackgroundDrawable(subjectLayout, R.drawable.text_input_focus_background)
            } else {
                hideKeyboard(subjectLayout)
                setBackgroundDrawable(subjectLayout, R.drawable.text_input_background)
            }
        }

        messageEditText.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                setBackgroundDrawable(messageLayout, R.drawable.message_input_type_shape)
            } else {
                hideKeyboard(messageLayout)
                setBackgroundDrawable(messageLayout, R.drawable.message_input_shape)
                showView(messageExpandIcon)
                collapseMessageView()
            }
        }
    }

    private fun addSelectedReceiverItem(receiver: ReceiverItem) {
        if (selectedReceiverViewMap.containsKey(receiver.id))
            return

        adapter!!.addSelectItem(receiver)
        val selectedReceiverItemView = SelectedReceiverItemView(this)
        selectedReceiverItemView.setReceiver(receiver) { removeSelectedReceiverItem(receiver.id) }
        addSelectedReceiverLayout.addView(selectedReceiverItemView)
        Log.d("addSelectedReceiverItem", "receiver id=" + receiver.id)
        selectedReceiverViewMap[receiver.id] = selectedReceiverItemView
        checkShowReceiverTitleView()
        checkMessageSend()
    }

    private fun removeSelectedReceiverItem(itemId: Int) {
        val view = selectedReceiverViewMap[itemId]
        val parent = view?.parent as ViewGroup
        parent.removeView(view)
        adapter!!.removeSelectItem(itemId)
        selectedReceiverViewMap.remove(itemId)
        checkShowReceiverTitleView()
        checkMessageSend()
    }

    private fun checkShowReceiverTitleView() {
        if (adapter!!.hasSelectedItem() || receiverAutoCompleteText.text.isNotEmpty())
            showTitleView(false, receiverTitleText, receiverTitleHintText)
        else
            showTitleView(true, receiverTitleText, receiverTitleHintText)
    }

    private fun checkMessageSend() {
        if (adapter!!.hasSelectedItem() && subjectEditText.text.isNotEmpty() && messageEditText.text.isNotEmpty())
            messageSendIcon.setImageResource(R.drawable.message_send_blue)
        else
            messageSendIcon.setImageResource(R.drawable.message_send_grey)
    }

    private fun showSubjectTitleView(show: Boolean) {
        showTitleView(show, subjectTitleText, subjectTitleHintText)
    }

    private fun showTitleView(show: Boolean, titleView: View, titleHintView: View) {
        if (show) {
            showView(titleView)
            hideView(titleHintView)
        } else {
            showView(titleHintView)
            hideView(titleView)
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
