package com.example.wendywu.startchatdemo.utils

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.wendywu.startchatdemo.R
import kotlinx.android.synthetic.main.activity_main.*

fun combineText(vararg texts: String): CharSequence {
    var result = StringBuilder()
    for (text in texts) {
        result = result.append(text)
    }
    return result
}

fun hideView(vararg views: View) {
    for (v in views) {
        v.visibility = View.GONE
    }
}

fun showView(vararg views: View) {
    for (v in views) {
        v.visibility = View.VISIBLE
    }
}

fun setBackgroundDrawable(view: View, id: Int) {
    view.background = (ContextCompat.getDrawable(view.context, id))

}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}