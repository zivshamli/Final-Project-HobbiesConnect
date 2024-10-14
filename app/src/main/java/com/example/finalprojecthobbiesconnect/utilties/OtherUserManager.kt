package com.example.finalprojecthobbiesconnect.utilties

import com.example.finalprojecthobbiesconnect.models.User

class OtherUserManager private constructor() {
    private var currentUser: User? = null

    // Set the current user
    fun setUser(user: User) {
        currentUser = user
    }

    // Get the current user
    fun getUser(): User? {
        return currentUser
    }

    // Clear the user data (logout)
    fun logout() {
        currentUser = null
    }
    companion object {
        @Volatile
        private var instance: OtherUserManager? = null

        // Initialize the UserManager with a context
        fun init(): OtherUserManager {
            return instance ?: synchronized(this) {
                instance ?: OtherUserManager().also { instance = it }
            }
        }

        // Get the singleton instance of UserManager
        fun getInstance(): OtherUserManager {
            return instance ?: throw IllegalStateException("UserManager must be initialized by calling init(context) before use.")
        }
    }

}