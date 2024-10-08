package com.example.finalprojecthobbiesconnect

import android.app.Application
import com.example.finalprojecthobbiesconnect.utilties.ImageLoader
import com.example.finalprojecthobbiesconnect.utilties.SignalManager
import com.example.finalprojecthobbiesconnect.utilties.MyActiveUserManager

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        SignalManager.init(this)
        ImageLoader.init(this)
        MyActiveUserManager.init()


    }

}