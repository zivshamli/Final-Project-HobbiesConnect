package com.example.finalprojecthobbiesconnect.utilties

import com.example.finalprojecthobbiesconnect.models.User

class MyActiveUserManager private constructor() {




    companion object {
        @Volatile
        private var instance: MyActiveUserManager? = null
        private var currentUser: User=User()

        // Initialize the UserManager with a context
        fun init(): MyActiveUserManager {
            return instance ?: synchronized(this) {
                instance ?: MyActiveUserManager().also { instance = it }
            }
        }






        // Function to set the current user
        fun setUser(user: User) {
            currentUser = user

        }

        // Function to get the current user
        fun getUser(): User {
            return currentUser
        }

        // Function to log out the user (clear user data)
        fun logout() {
            currentUser =User()
        }
    }
}