package com.example.uberbookingexperience.ui.util

import java.text.DecimalFormat

fun Float.toINRString(): String {
    return try {
        val df = DecimalFormat("#,##0")
        df.format(this) + " Kz"
    } catch(ex: Exception) {
        ""
    }
}