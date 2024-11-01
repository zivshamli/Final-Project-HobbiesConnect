package com.example.finalprojecthobbiesconnect

import android.app.Application
import com.example.finalprojecthobbiesconnect.utilties.ImageLoader
import com.example.finalprojecthobbiesconnect.utilties.SignalManager
import com.example.finalprojecthobbiesconnect.utilties.MyActiveUserManager
import com.example.finalprojecthobbiesconnect.utilties.OtherUserManager
import com.example.finalprojecthobbiesconnect.utilties.SharedPreferencesManagerV3

class App: Application() {
    var read_notification_flag: Boolean = true
    var read_chat_flag: Boolean = true
    override fun onCreate() {
        super.onCreate()
        SignalManager.init(this)
        ImageLoader.init(this)
        MyActiveUserManager.init()
        OtherUserManager.init()
        SharedPreferencesManagerV3.init(this)


    }

}