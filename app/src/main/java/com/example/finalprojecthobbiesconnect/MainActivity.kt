package com.example.finalprojecthobbiesconnect

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.finalprojecthobbiesconnect.utilties.SignalManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {
    private lateinit var emailTextField: TextInputEditText
    private lateinit var passwordTextField: TextInputEditText
    private lateinit var loginBTN: MaterialButton
    private lateinit var signUpBTN: MaterialButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        findViews()
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
    }


    private fun checkTextFields():Boolean {
        if (emailTextField.text.toString().isEmpty() && passwordTextField.text.toString().isEmpty()) {
            SignalManager.getInstance().vibrateAndToast("email and password missing!")
            return false
        }

       else if (emailTextField.text.toString().isEmpty() ) {
            SignalManager.getInstance().vibrateAndToast("email missing!")
            return false

        }
        else if (passwordTextField.text.toString().isEmpty()) {
            SignalManager.getInstance().vibrateAndToast("password missing!")
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


}

