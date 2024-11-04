package com.example.finalprojecthobbiesconnect

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.finalprojecthobbiesconnect.models.User
import com.example.finalprojecthobbiesconnect.utilties.Constants
import com.example.finalprojecthobbiesconnect.utilties.FuncUtlis
import com.example.finalprojecthobbiesconnect.utilties.SignalManager
import com.example.finalprojecthobbiesconnect.utilties.MyActiveUserManager
import com.example.finalprojecthobbiesconnect.utilties.SharedPreferencesManagerV3
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MainActivity : AppCompatActivity() {
    private lateinit var emailTextField: TextInputEditText
    private lateinit var passwordTextField: TextInputEditText
    private lateinit var loginBTN: MaterialButton
    private lateinit var signUpBTN: MaterialButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        findViews()
        FuncUtlis.setupUI(this, findViewById(android.R.id.content))
        checkPreferences()
        initViews()

    }

    private fun checkPreferences() {
        //check if user is logged in in the past and not logout
        val userEmail =SharedPreferencesManagerV3.getInstance().getString(Constants.USERID_KEY,"")
        if(userEmail!="") {
            loadUserData(userEmail)
            changeActivity()
        }
    }


    private fun findViews() {
        emailTextField = findViewById(R.id.email_text_field)
        passwordTextField = findViewById(R.id.password_text_field)
        loginBTN = findViewById(R.id.login_BTN)
        signUpBTN = findViewById(R.id.sign_up_BTN)
    }
    private fun initViews() {
        signUpBTN.setOnClickListener { changeToSignUp()}
        loginBTN.setOnClickListener { login()}

    }

    private fun login() {
        if (!checkTextFields()) {
            return
        }
        signInUser(emailTextField.text.toString().lowercase(), passwordTextField.text.toString())

    }


    private fun checkTextFields():Boolean {
        if (emailTextField.text.toString().isEmpty() && passwordTextField.text.toString().isEmpty()) {
            SignalManager.getInstance().vibrateAndToast(Constants.ALERT1)
            return false
        }

        else if (emailTextField.text.toString().isEmpty() ) {
            SignalManager.getInstance().vibrateAndToast(Constants.ALERT2)
            return false

        }
        else if (passwordTextField.text.toString().isEmpty()) {
            SignalManager.getInstance().vibrateAndToast(Constants.ALERT3)
            return false
        }
        return true
    }

    private fun changeToSignUp() {
        val intent = Intent(this, SignInActivity::class.java)
        val b = Bundle()
        intent.putExtras(b)
        startActivity(intent)
        finish()
    }
    private fun changeActivity() {
        val intent = Intent(this, NavigationActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun signInUser(email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    if (task.result?.user == null) {
                        SignalManager.getInstance().vibrateAndToast(Constants.ALERT_USER_NOT_FOUND)

                    }
                    else {
                        val user = FirebaseAuth.getInstance().currentUser
                        val userEmail = user!!.email!!.replace(".",",")
                         loadUserData(userEmail)
                        MyActiveUserManager.getUser().email=userEmail.replace(",",".")
                        SharedPreferencesManagerV3.getInstance().putString(Constants.USERID_KEY,userEmail)
                        changeActivity()
                        SignalManager.getInstance().toast(Constants.SIGN_IN_SUCCESSFUL)
                    }
                } else {
                    SignalManager.getInstance().vibrateAndToast("Sign in failed: ${task.exception?.message}")
                }
            }
    }

    private fun loadUserData(userEmail: String) {
        val database=FirebaseDatabase.getInstance()

        val userRef=database.getReference(Constants.USERS_REF).child(userEmail)
        userRef.addValueEventListener(object :ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                if (user != null) {
                    if(user.email==MyActiveUserManager.getUser().email||MyActiveUserManager.getUser().email=="") {
                        MyActiveUserManager.setUser(user)
                        val app=applicationContext as App
                        app.read_notification_flag=true
                    }
                } else {
                    SignalManager.getInstance().vibrateAndToast(Constants.ALERT_LOAD_USER)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                SignalManager.getInstance().vibrateAndToast(Constants.ALERT_LOAD_USER)
            }
        })







    }


}

