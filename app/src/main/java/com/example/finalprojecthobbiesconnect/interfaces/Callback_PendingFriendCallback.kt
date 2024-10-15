package com.example.finalprojecthobbiesconnect.interfaces

import com.example.finalprojecthobbiesconnect.models.User

interface Callback_PendingFriendCallback {
    fun pendingFriendClick(user: User, position: Int)

}