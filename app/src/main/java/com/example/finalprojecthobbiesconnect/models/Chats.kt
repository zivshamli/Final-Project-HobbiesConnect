package com.example.finalprojecthobbiesconnect.models

data class Chats(

    private var messages :MutableList<Message> = mutableListOf(),
    val participantsStatus: MutableMap<String, Boolean> = linkedMapOf()


):Comparable <Chats>

{
    fun addMessage(message: Message) {
        messages.add(message)
        // sort messages by timestamp
        messages.sortBy { it.timestamp }
    }

    fun getMessages(): List<Message> {
        return messages
    }

    override fun compareTo(other: Chats): Int {
        val thisLastMessageTime = this.messages.maxOfOrNull { it.timestamp } ?: Long.MIN_VALUE
        val otherLastMessageTime = other.messages.maxOfOrNull { it.timestamp } ?: Long.MIN_VALUE
        return thisLastMessageTime.compareTo(otherLastMessageTime)


    }
}

