package com.secret.santa.views

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.secret.santa.R
import kotlinx.android.synthetic.main.activity_login.*


class LoginSSA : AppCompatActivity() {

    companion object{
        val USER_NAME = "USER_NAME"
    }



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




    private fun Login() {


        val regEmail = findViewById<TextView>(R.id.inptEmail);
        val regPasswrd = findViewById<TextView>(R.id.inptPassword);

        if (TextUtils.isEmpty(regEmail.text)) {
            regEmail.setError("Email is verplicht!")
        } else if (TextUtils.isEmpty(regPasswrd.text)) {
            regPasswrd.setError("Wachtwoord is verplicht!")
        } else {
              FirebaseAuth.getInstance().signInWithEmailAndPassword(regEmail.text.toString().trim(), regPasswrd.text.toString().trim())
                  .addOnCompleteListener{
                      if(!it.isSuccessful){
                          return@addOnCompleteListener
                      } else {

                          val intent = Intent(this, MainSSA::class.java)
                          intent.putExtra(USER_NAME, FirebaseAuth.getInstance().currentUser?.displayName)
                          intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK);
                          startActivity(intent);
                      }
                  }
                  .addOnFailureListener{
                      regEmail.setError("Email/Wachtwoord verkeerd!")
                      regPasswrd.setError("Email/Wachtwoord verkeerd!")
            }



          /*
            ParseUser.logInInBackground(
                regEmail.text.toString().trim(),regPasswrd.text.toString().trim()
            ) { user, e ->
                if (user != null) {

                } else {
                    Log.d("LOGIN RESULT", "${e.message}");
                }
            }
          */
        }
    }


}
