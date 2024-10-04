package com.example.finalprojecthobbiesconnect.models

data class User(
    var username:String = "",
    var email:String = "",
     var selectedYear:Int = 0,
    var password:String = "",
    var profilePhoto:String = "",
    var friendsList: MutableList<String> = mutableListOf(),
    var pendingFriendsList: MutableList<String> = mutableListOf(),
    var chatList: MutableList<String> = mutableListOf()


    )
