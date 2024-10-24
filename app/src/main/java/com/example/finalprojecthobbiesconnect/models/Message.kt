package com.example.finalprojecthobbiesconnect.models

data class Message (
     var senderEmail: String="",
     var content: String="",
     var timestamp: Long=System.currentTimeMillis()
):Comparable<Message> {
    override fun compareTo(other: Message): Int {
        return this.timestamp.compareTo(other.timestamp)
    }

}


