package com.example.secret_santa_app.views


import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.secret_santa_app.R
import com.example.secret_santa_app.services.MusicServiceSSA
import com.parse.*
import kotlinx.android.synthetic.main.activity_register.*;


class RegisterSSA() : AppCompatActivity() {

    var serviceItent = Intent();

    @Override
    // Algemene Oncreate Functie om layout aan te roepen
    override fun onCreate(savedInstanceState: Bundle?) {
        // overerven van param
        super.onCreate(savedInstanceState)
        // het definiëren van de layout keuze
        setContentView(R.layout.activity_register)
        // de juiste config. aanroepen om te kunnen verbinden met DB
        ParseInstallation.getCurrentInstallation().saveInBackground();
        alreadyHaveAccountText.setOnClickListener { this.HaveAcc(); }
        btnRegister.setOnClickListener {
            try {
                this.Registeren();
            } catch (ex: Exception) {
            }
        }
        // MUSICA MAESTROOOO
        // hier maken we service effectief aan (onCreate method)
        serviceItent = Intent(applicationContext, MusicServiceSSA::class.java);
        startService(serviceItent);
    }


    // effectief registreren naar parseDB
    private fun Registeren() {
        val regName = findViewById<TextView>(R.id.inptName);
        val regEmail = findViewById<TextView>(R.id.inptEmail);
        val regPasswrd = findViewById<TextView>(R.id.inptPassword);
        if (TextUtils.isEmpty(regName.text)) {
            regName.setError("Naam is verplicht!")
        } else if (TextUtils.isEmpty(regEmail.text)) {
            regEmail.setError("Email is verplicht!")
        } else if (TextUtils.isEmpty(regPasswrd.text)) {
            regPasswrd.setError("Wachtwoord is verplicht!")
        } else {
            // create new ParseUser to be stored in DB
            val user = ParseUser();
            // add params to object
            user.username = regEmail.text.toString().trim();
            user.setPassword(regPasswrd.text.toString().trim())
            user.put("Naam", regName.text.toString().trim());
            // open connection and try to sign up

            user.signUpInBackground(SignUpCallback { e ->
                if (e == null) {
                    // creeër nieuwe 'intent' naar de session classe
                    // wat dan op zijn beurt de actieve layout rendering zal aanpassen
                    val intent = Intent(this, MainSSA::class.java)
                    startActivity(intent);
                } else {
                    ParseUser.logOut();
                    Toast.makeText(this, e.message, Toast.LENGTH_LONG).show();
                }
            })

        }

    }

    // veranderen van actieve layout view
    private fun HaveAcc() {
        val intent = Intent(this, LoginSSA::class.java)
        startActivity(intent);
    }

}
