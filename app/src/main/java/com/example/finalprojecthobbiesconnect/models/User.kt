package com.example.finalprojecthobbiesconnect.models

data class User(
    var username:String = "",
    var email:String = "",
     var birthyear:Int = 0,
    var profilePhoto:String = "",
    var friendsList: MutableList<String> = mutableListOf(),
    var pendingFriendsList: MutableList<String> = mutableListOf(),
    var chatList: MutableList<String> = mutableListOf(),
    var hobbies: MutableList<String> = mutableListOf(),
    var isRead:Boolean = true


    )
