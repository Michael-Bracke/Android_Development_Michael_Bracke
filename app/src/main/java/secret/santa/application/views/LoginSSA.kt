package com.secret.santa.views

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.secret.santa.R
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*


class LoginSSA : AppCompatActivity() {

    companion object{
        val USER_NAME = "USER_NAME"
        val TAG = "LOGIN"
    }
    // FACEBOOK AUTH INSTANCE
    // val callbackManager = CallbackManager.Factory.create();



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // change to the activity login view
        setContentView(R.layout.activity_login)
        // create on click event for the login btn
        textRegistreer.setOnClickListener { RegView(); }
        btnInloggen.setOnClickListener {  Login(); }

        /* FACEBOOK AUTH INSTANCE
        val EMAIL = "email"

        val loginButton = findViewById<View>(R.id.btnFacebookLogin) as LoginButton
        loginButton.setReadPermissions(Arrays.asList(EMAIL))
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
            override fun onSuccess(loginResult: LoginResult?) {
                Log.d(TAG, "signInWithCredential:success")
                val user = FirebaseAuth.getInstance().currentUser
                val intent = Intent(this@LoginSSA, MainSSA::class.java)
                intent.putExtra(USER_NAME, user?.displayName)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }

            override fun onCancel() {
                Log.d(TAG, "CANCE3LLED")
            }

            override fun onError(exception: FacebookException) {
                Log.d(TAG,exception.message.toString())
            }
        })



  */


    }

/*FACEBOOK AUTH INSTANCE
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
  */

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
