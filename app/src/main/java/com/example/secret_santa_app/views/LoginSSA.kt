package com.example.secret_santa_app.views

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.secret_santa_app.R
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_login.*


class LoginSSA : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // change to the activity login view
        setContentView(R.layout.activity_login)
        // create on click event for the login btn
        textRegistreer.setOnClickListener { RegView(); }
        btnInloggen.setOnClickListener {  Login(); }
        // Write a message to the database


    }

    private fun RegView(){
        val intent = Intent(this, RegisterSSA::class.java);
        startActivity(intent);
    }

    fun loginUser() {
        ParseUser.logInInBackground("<userName>", "<password>") { user, e ->
            if (user != null) {
                // Hooray! The user is logged in.
            } else {
                // Signup failed. Look at the ParseException to see what happened.
            }
        }
    }


    private fun Login() {


        val regEmail = findViewById<TextView>(R.id.inptEmail);
        val regPasswrd = findViewById<TextView>(R.id.inptPassword);

        if (TextUtils.isEmpty(regEmail.text)) {
            regEmail.setError("Email is verplicht!")
        } else if (TextUtils.isEmpty(regPasswrd.text)) {
            regPasswrd.setError("Wachtwoord is verplicht!")
        } else {
            Log.d("login", "${regEmail.text.toString()}");
            Log.d("login", "${regPasswrd.text.toString()}");


            ParseUser.logInInBackground(
                regEmail.text.toString().trim(), regPasswrd.text.toString().trim()
            ) { user, e ->
                if (user != null) {
                    val intent = Intent(this, MainSSA::class.java)
                    startActivity(intent);
                } else {
                    Log.d("LOGIN RESULT", "${e.message}");
                }
            }



          /*
            ParseUser.logInInBackground(
                regEmail.text.toString().trim(),regPasswrd.text.toString().trim()
            ) { user, e ->
                if (user != null) {
                    val intent = Intent(this, SessionSSA::class.java)
                    startActivity(intent);
                } else {
                    Log.d("LOGIN RESULT", "${e.message}");
                }
            }
          */
        }
    }


}
