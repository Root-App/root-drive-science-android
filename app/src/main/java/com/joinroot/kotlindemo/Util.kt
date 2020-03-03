package com.joinroot.kotlindemo

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.Menu
import android.view.inputmethod.InputMethodManager
import java.text.SimpleDateFormat
import java.util.*


fun copyToClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val data = ClipData.newPlainText("Trip Tracker log", text)
    clipboard.setPrimaryClip(data)
}

fun hideKeyboard(activity: Activity) {
    val inputManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
}

fun Menu.add(itemId: Int, title: CharSequence) {
    add(Menu.NONE, itemId, Menu.NONE, title)
}
