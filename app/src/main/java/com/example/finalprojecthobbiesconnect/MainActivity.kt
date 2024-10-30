package com.example.finalprojecthobbiesconnect

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.finalprojecthobbiesconnect.models.User
import com.example.finalprojecthobbiesconnect.utilties.Constants
import com.example.finalprojecthobbiesconnect.utilties.FuncUtlis
import com.example.finalprojecthobbiesconnect.utilties.SignalManager
import com.example.finalprojecthobbiesconnect.utilties.MyActiveUserManager
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
        initViews()

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
            SignalManager.getInstance().vibrateAndToast(Constants.ALRET1)
            return false
        }

        else if (emailTextField.text.toString().isEmpty() ) {
            SignalManager.getInstance().vibrateAndToast(Constants.ALRET2)
            return false

        }
        else if (passwordTextField.text.toString().isEmpty()) {
            SignalManager.getInstance().vibrateAndToast(Constants.ALRET3)
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
                        SignalManager.getInstance().vibrateAndToast("User not found")

                    }
                    else {
                        val user = FirebaseAuth.getInstance().currentUser
                        val userEmail = user!!.email!!.replace(".",",")
                         loadUserData(userEmail)
                        MyActiveUserManager.getUser().email=userEmail.replace(",",".")
                        changeActivity()
                        SignalManager.getInstance().toast("Sign in successful!")
                    }
                } else {
                    SignalManager.getInstance().vibrateAndToast("Sign in failed: ${task.exception?.message}")
                }
            }
    }

    private fun loadUserData(userEmail: String) {
        val database=FirebaseDatabase.getInstance()

        val userRef=database.getReference("users").child(userEmail)
        userRef.addValueEventListener(object :ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                if (user != null) {
                    if(user.email==MyActiveUserManager.getUser().email||MyActiveUserManager.getUser().email=="") {
                        MyActiveUserManager.setUser(user)
                    }
                } else {
                    SignalManager.getInstance().vibrateAndToast("Failed to load user data")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                SignalManager.getInstance().vibrateAndToast("Failed to load user data")
            }
        })







    }


}

