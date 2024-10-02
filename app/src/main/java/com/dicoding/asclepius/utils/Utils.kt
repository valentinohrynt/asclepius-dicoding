package com.dicoding.asclepius.utils

import android.content.Context

object Utils
{
    fun showToast(context: Context, message: String){
        android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show()
    }
    fun convertToPercent(value: Float): String {
        val percentage = value * 100
        val roundedPercentage = kotlin.math.round(percentage).toInt()
        return "$roundedPercentage%"
    }
}