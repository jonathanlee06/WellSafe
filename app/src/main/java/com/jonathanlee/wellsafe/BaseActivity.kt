package com.jonathanlee.wellsafe

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
    protected fun showQuitDialog(
        context: Context,
        cancelable: Boolean = false,
        title: String,
        message: String,
        positiveMsg: String,
        negativeMsg: String
    ) {
        val builder = AlertDialog.Builder(context)
        builder.apply {
            setCancelable(cancelable)
            setTitle(title)
            setMessage(message)
            setPositiveButton(positiveMsg) { dialog, which -> finish() }
            setNegativeButton(negativeMsg) { dialog, which -> dialog.cancel() }
            create()
            show()
        }
    }
}