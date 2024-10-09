package com.example.finalprojecthobbiesconnect.interfaces

import com.example.finalprojecthobbiesconnect.models.User

interface Callback_SearchUserCallback {
    fun searchUserClick(user: User, position: Int)
}