package com.alalodev.misdeudas.presentation.extensions

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long?.millisToDate(): String{
    this ?: return ""

    return try {
        val date = Date(this)
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        sdf.format(date)
    }catch (e:Exception){
        ""
    }
}