package com.example.finalprojecthobbiesconnect.utilties

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.example.finalprojecthobbiesconnect.models.Chats
import com.example.finalprojecthobbiesconnect.models.User
import com.google.firebase.database.FirebaseDatabase

class FuncUtlis {
    companion object{

        fun loadPartner(chat: Chats, onComplete: (User?) -> Unit) {
            lateinit var partnerEmail: String
            for (userEmail in chat.participantsStatus.keys){
                if (userEmail.replace(",",".") != MyActiveUserManager.getUser().email)
                    partnerEmail = userEmail
            }
            val database= FirebaseDatabase.getInstance()
            val userRef=database.getReference("users").child(partnerEmail)
            userRef.get().addOnSuccessListener { dataSnapshot ->
                val partner = dataSnapshot.getValue(User::class.java)
                onComplete(partner)
            }
            .addOnFailureListener {
                // Handle failure
                onComplete(null)
            }

        }

        fun hideKeyboard(view: View) {
            val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
        private fun hideKeyboard(activity: Activity) {
            val view = activity.currentFocus
            view?.let {
                val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }

        @SuppressLint("ClickableViewAccessibility")
        fun setupUI(activity: Activity, rootView: View) {
            if (rootView !is EditText) {
                rootView.setOnTouchListener { _, _ ->
                    hideKeyboard(activity)
                    rootView.clearFocus()
                    false
                }
            }

            if (rootView is ViewGroup) {
                for (i in 0 until rootView.childCount) {
                    val innerView = rootView.getChildAt(i)
                    setupUI(activity, innerView)
                }
            }
        }
        

    }
}