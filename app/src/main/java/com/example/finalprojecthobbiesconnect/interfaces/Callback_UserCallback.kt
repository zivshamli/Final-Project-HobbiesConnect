package com.example.finalprojecthobbiesconnect.interfaces

import com.example.finalprojecthobbiesconnect.models.User

interface Callback_UserCallback {
    fun userClick(user: User, position: Int)
}