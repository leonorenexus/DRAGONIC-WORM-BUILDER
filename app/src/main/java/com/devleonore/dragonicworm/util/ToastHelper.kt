package com.devleonore.dragonicworm.util

import android.content.Context
import android.widget.Toast

object ToastHelper {
    enum class Type { SUCCESS, ERROR, INFO }

    fun show(context: Context, message: String, type: Type = Type.INFO) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
