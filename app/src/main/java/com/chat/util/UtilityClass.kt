package com.chat.util

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.chat.R

object UtilityClass {

    fun showAlert(
        context: Context,
        title: String = "",
        message: String = "",
        onYes: Runnable? = null,
        onNo: Runnable? = null
    ) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle(title)
        alertDialogBuilder.setMessage(message)
        alertDialogBuilder.setPositiveButton(R.string.yes){
            dialog,which-> onYes?.run()
        }
        alertDialogBuilder.setNegativeButton("no"){
            dialog,which->onNo?.run()
        }
        alertDialogBuilder.show()
    }

    fun getBackEndHost():String{
        return "https://forum-express-ms-production.up.railway.app"
    }
}