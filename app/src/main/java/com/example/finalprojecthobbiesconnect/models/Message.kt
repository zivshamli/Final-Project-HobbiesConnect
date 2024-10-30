package com.example.finalprojecthobbiesconnect.models

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Message (
     var senderEmail: String="",
     var content: String="",
     var date: String = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()),
     var timestamp: Long=System.currentTimeMillis()
):Comparable<Message> {
    override fun compareTo(other: Message): Int {
        return this.timestamp.compareTo(other.timestamp)
    }

}


