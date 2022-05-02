package com.example.parstagram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.parse.ParseObject
import com.parse.ParseUser
import kotlin.math.sign

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Check if there's a user logged in
        //If there is, go to MainActivity
        if(ParseUser.getCurrentUser() != null){
            goToMainActivity()
        }

        //Testing Parse
        /*
        val firstObject = ParseObject("FirstClass")
        firstObject.put("message","Hey ! First message from android. Parse is now connected")
        firstObject.saveInBackground {
            if (it != null){
                it.localizedMessage?.let { message -> Log.e("MainActivity", message) }
            }else{
                Log.d("MainActivity","Object saved.")
            }
        }
        */

        findViewById<Button>(R.id.login_button).setOnClickListener{
            val username = findViewById<EditText>(R.id.et_username).text.toString()
            val password = findViewById<EditText>(R.id.et_password).text.toString()
            loginUser(username, password)
        }

        findViewById<Button>(R.id.signupBtn).setOnClickListener{
            val username = findViewById<EditText>(R.id.et_username).text.toString()
            val password = findViewById<EditText>(R.id.et_password).text.toString()
            signUpUser(username, password)
        }
    }

    private fun signUpUser(username: String, password: String){
        // Create the ParseUser
        val user = ParseUser()

        // Set fields for the user to be created
        user.setUsername(username)
        user.setPassword(password)

        user.signUpInBackground { e ->
            if (e == null) {
                // User has successfully created a new account
                Log.i(TAG, "Successfully registered")
                Toast.makeText(this, "Successfully registered and logged in", Toast.LENGTH_SHORT).show()
                goToMainActivity()
            } else {
                // Sign up didn't succeed. Look at the ParseException
                // to figure out what went wrong
                Toast.makeText(this, "Sign up was not successful", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }

    private fun loginUser(username: String, password: String){
        ParseUser.logInInBackground(
            username, password, ({ user, e ->
                if (user != null) {
                    // Hooray! The user is logged in
                    Log.i(TAG, "Successfully logged in!")
                    goToMainActivity()
                } else {
                    e.printStackTrace()
                    //Toast.makeText(this,
                    //    "Username - " + username + "Password - " + password, Toast.LENGTH_SHORT).show()
                    Toast.makeText(this, "Error logging in", Toast.LENGTH_SHORT).show()
                    Log.i(TAG, "Log in failed.")
                }
            })
        )
    }

    private fun goToMainActivity(){
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object{
        val TAG = "LoginActivity"
    }
}