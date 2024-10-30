package com.example.finalprojecthobbiesconnect.models

import java.util.UUID

data class Chats(
    val messages: MutableMap<String, Message> = mutableMapOf(),

    val participantsStatus: MutableMap<String, Boolean> = linkedMapOf()
):Comparable<Chats>

    {
    fun addMessage(message: Message)
    {
        messages[UUID.randomUUID().toString()] = message
    }


        override fun compareTo(other: Chats): Int {

            if(other.messages.values.maxOrNull()==null||this.messages.values.maxOrNull()==null)
                return -1
            if(other.messages.values.maxOrNull()!!.timestamp>this.messages.values.maxOrNull()!!.timestamp)
                return 1

            return -1

        }


    }
