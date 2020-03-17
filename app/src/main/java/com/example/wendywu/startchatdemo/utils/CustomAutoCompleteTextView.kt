package com.example.wendywu.startchatdemo.utils

import android.annotation.SuppressLint
import android.content.Context
import android.text.Selection
import android.util.AttributeSet
import android.widget.AutoCompleteTextView

@SuppressLint("AppCompatCustomView")
class CustomAutoCompleteTextView : AutoCompleteTextView {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {}

    override fun replaceText(text: CharSequence) {
        clearComposingText()
        setText("")
        val spannable = getText()
        Selection.setSelection(spannable, spannable.length)
    }
}