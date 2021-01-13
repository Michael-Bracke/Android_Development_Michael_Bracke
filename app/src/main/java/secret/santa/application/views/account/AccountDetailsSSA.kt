package com.secret.santa.views


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.secret.santa.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_account_details.*
import secret.santa.application.services.MusicServiceSSA

class AccountDetailsSSA() : AppCompatActivity() {


    @Override
    // Algemene Oncreate Functie om layout aan te roepen
    override fun onCreate(savedInstanceState: Bundle?) {
        // overerven van param
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Account"
        // de juiste config. aanroepen om te kunnen verbinden met DB
        // het definiëren van de layout keuze
        setContentView(R.layout.activity_account_details);
        Picasso.get().load(FirebaseAuth.getInstance().currentUser?.photoUrl).into(imgProfileAccount);
        tvName.text = FirebaseAuth.getInstance().currentUser?.displayName

        // set on click listeners
        //TODO creëer nieuwe activities die reageren op de buttons om de accounts zaken aan te passen

    }




}
