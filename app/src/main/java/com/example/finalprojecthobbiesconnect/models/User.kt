package com.example.finalprojecthobbiesconnect.models

import java.time.LocalDate

data class User(
    var username:String = "",
    var email:String = "",
     var birthyear:Int = 0,
    var profilePhoto:String = "",
    var friendsList: MutableList<String> = mutableListOf(),
    var pendingFriendsList: MutableList<String> = mutableListOf(),
    var chatList: MutableList<String> = mutableListOf(),
    var hobbies: MutableList<String> = mutableListOf(),
    var isReadPend:Boolean = true,



    ):Comparable<User>
{
    fun getAge():Int
    {
        return LocalDate.now().year - birthyear
    }

    override fun compareTo(other: User): Int {
        return this.username.compareTo(other.username)
    }
}
