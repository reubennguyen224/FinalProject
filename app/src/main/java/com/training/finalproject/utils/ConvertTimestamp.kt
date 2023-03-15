package com.training.finalproject.utils

import java.text.SimpleDateFormat
import java.util.*

fun convertTimestamp(time: String): String{
    var dateString = ""
    val timeLong = time.toLong()
    if ((System.currentTimeMillis() - timeLong) <= 172800000){
        if ((System.currentTimeMillis() - timeLong) <= 86400000){
            val date = Date(timeLong)
            val sdf = SimpleDateFormat("HH:mm")
            sdf.timeZone = TimeZone.getDefault()
            dateString = sdf.format(date)
        } else dateString = "Yesterday"
    } else if ((System.currentTimeMillis() - timeLong) > 172800000){
        val date = Date(timeLong)
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        dateString = sdf.format(date)
    }
    return dateString
}