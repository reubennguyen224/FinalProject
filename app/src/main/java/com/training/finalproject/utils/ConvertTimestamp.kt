package com.training.finalproject.utils

import java.text.SimpleDateFormat
import java.util.*

fun convertTimestamp(time: String): String{
    var dateString = ""
    val timeLong = time.toLong()
    if ((System.currentTimeMillis()*10 - timeLong) <= 172800000){
        if ((System.currentTimeMillis()*10 - timeLong) <= 86400000){
            val date = Date(timeLong)
            val sdf = SimpleDateFormat("HH:mm", Locale.CHINA)
            sdf.timeZone = TimeZone.getDefault()
            dateString = sdf.format(date)
        } else dateString = "Yesterday"
    } else{
        val day = (System.currentTimeMillis()*10 - timeLong) / 86400000
        if (day in 3..30){
            dateString = "$day days ago"
        } else if (day>30){
            val date = Date(timeLong)
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.CHINA)
            dateString = sdf.format(date)
        }
    }
    return dateString
}