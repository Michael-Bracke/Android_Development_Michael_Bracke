package com.secret.santa.views


import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.parse.ParseInstallation
import com.secret.santa.R
import kotlinx.android.synthetic.main.activity_register.*
import secret.santa.application.services.MusicServiceSSA
import java.util.*


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

        // set een onclick event listerer voor de 'selecteer foto' button
        fotoSelector.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0);
        }
        // MUSICA MAESTROOOO
        // hier maken we service effectief aan (onCreate method)
        serviceItent = Intent(applicationContext, MusicServiceSSA::class.java);
        startService(serviceItent);
    }
    // init de uri om later controle op te doen tijdens het registreren.
    var selectedFotoUri: Uri? = null
    // init base params van form


    // gets called wanneer de image wordt gekozen
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
       super.onActivityResult(requestCode, resultCode, data)
       if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
           // doorgaan en kijken wat de geselecteerde image blijkt te zijn
           selectedFotoUri = data.data;
           val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedFotoUri)
           val bitmapDrawable = BitmapDrawable(bitmap)
           fotoSelector.background = bitmapDrawable;
           fotoSelector.text = "";
       }
    }


    // effectief registreren naar parseDB
    private fun Registeren() {
        val regName = findViewById<TextView>(R.id.inptName)
        val regEmail = findViewById<TextView>(R.id.inptEmail)
        val regPasswrd = findViewById<TextView>(R.id.inptPassword)
        if (TextUtils.isEmpty(regName.text)) {
            regName.setError("Naam is verplicht!")
        } else if (TextUtils.isEmpty(regEmail.text)) {
            regEmail.setError("Email is verplicht!")
        } else if (TextUtils.isEmpty(regPasswrd.text)) {
            regPasswrd.setError("Wachtwoord is verplicht!")
        } else {
            // create new ParseUser to be stored in DB
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(regEmail.text.toString().trim(),regPasswrd.text.toString().trim())
                .addOnCompleteListener{
                if(!it.isSuccessful) {
                    it.addOnFailureListener {
                        Log.d("USERCREATION", it.message.toString())
                    }
                    regPasswrd.setError(it.toString())
                    return@addOnCompleteListener
                }
                    uploadImageToFirebaseStorage()
                    // wat dan op zijn beurt de actieve layout rendering zal aanpassen

            }
                .addOnFailureListener{
                    regEmail.setError(it.message)
                }

        }

    }

    // veranderen van actieve layout view
    private fun HaveAcc() {
        val intent = Intent(this, LoginSSA::class.java)
        startActivity(intent);
    }

    private fun uploadImageToFirebaseStorage(){
        // user dont have to choose a picture, choose standard avater when not chosen anything
        // TODO: Give user the option in a latter version to change picture
        if(selectedFotoUri == null) {
                FirebaseStorage.getInstance().getReference("/images/ssst_santa_img.jpg").downloadUrl.addOnSuccessListener {
                    saveUserToFireBaseDatabase(it.toString());
                }
        } else {

            // uploaden van files
            val fileName = UUID.randomUUID().toString()
            val fileRef = FirebaseStorage.getInstance().getReference("/images/$fileName")
            Log.d("DATABASE", "PROBEREN FILE TE UPLOADEN -> ${selectedFotoUri.toString()}")
            fileRef.putFile(selectedFotoUri!!)
                .addOnSuccessListener {
                    fileRef.downloadUrl.addOnSuccessListener {
                        saveUserToFireBaseDatabase(it.toString());
                    }
                }
                .addOnFailureListener {
                    Log.d("UPLOAD IMAGE", "Fail? -> ${it.toString()}")
                }
        }
    }

    private fun saveUserToFireBaseDatabase(profileImageUrl : String){

        val regName = findViewById<TextView>(R.id.inptName).text.toString()

        // use the shared user profiles to save personal data from user

        val user = FirebaseAuth.getInstance().currentUser
        val profileUpdates =
            UserProfileChangeRequest.Builder()
                .setDisplayName(regName)
                .setPhotoUri(Uri.parse(profileImageUrl))
                .build()
        if(user != null) {
            user.updateProfile(profileUpdates)
                .addOnSuccessListener {
                    Log.d("USERCREATION" , "SUCCESVOL OPGESLAGEN")
                    val intent = Intent(this, MainSSA::class.java)
                    // deze stap is belangerijk mits dit er voor gaat zorgen dat de user eens hij is ingelogd
                    // NIET weer terug gaat naar het regisratieformulier met de 'terug' toets
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
        }





    }

}



class User(val uid:String, val name:String, val profileImageUrl: String)
