package com.example.wendywu.startchatdemo.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.support.v4.content.ContextCompat
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import java.io.IOException

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

fun setImageDrawable(view: ImageView, id: Int) {
    view.setImageResource(id)
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun getJsonDataFromAsset(context: Context, fileName: String): String? {
    val jsonString: String
    try {
        jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
    } catch (ioException: IOException) {
        ioException.printStackTrace()
        return null
    }
    return jsonString
}

fun Context.drawableIdByName(resourceName: String): Int {
    return resIdByName(resourceName, "drawable")
}

fun Context.resIdByName(resIdName: String?, resType: String): Int {
    resIdName?.let {
        return resources.getIdentifier(it, resType, packageName)
    }
    throw Resources.NotFoundException()
}

fun getId(resourceName: String, c: Class<*>): Int {
    return try {
        val idField = c.getDeclaredField(resourceName)
        idField.getInt(idField)

    } catch (e: java.lang.Exception) {
        throw java.lang.RuntimeException("No resource ID found for: "
                + resourceName + " / " + c, e)
    }
}

fun isEmailValid(email: CharSequence): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

