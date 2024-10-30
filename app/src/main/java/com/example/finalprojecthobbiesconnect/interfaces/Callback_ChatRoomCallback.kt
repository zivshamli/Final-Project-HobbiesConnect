package com.example.finalprojecthobbiesconnect.interfaces

import com.example.finalprojecthobbiesconnect.models.Chats

interface Callback_ChatRoomCallback {
    fun chatRoomClicked(chatRoom: Chats, position:Int)
}