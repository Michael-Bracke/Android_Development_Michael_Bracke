package com.secret.santa.views


import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import com.parse.ParseInstallation
import com.secret.santa.R
import com.secret.santa.databinding.ActivityRegisterBinding
import secret.santa.application.extensions.isMyServiceRunning
import secret.santa.application.services.MusicServiceSSA
import java.util.*
import android.widget.CompoundButton
import com.google.firebase.FirebaseApp
import secret.santa.application.SQLite.DbAdapter
import secret.santa.application.services.AuthService


class RegisterSSA() : AppCompatActivity() {

    var serviceItent = Intent();
    var helper: DbAdapter? = null
    var AuthService: AuthService? = null
    private lateinit var binding: ActivityRegisterBinding
    @Override
    // Algemene Oncreate Functie om layout aan te roepen
    override fun onCreate(savedInstanceState: Bundle?) {

        // overerven van param
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        // het definiëren van de layout keuze
        setContentView(binding.root)
        FirebaseApp.initializeApp(binding.root.context)
        // de juiste config. aanroepen om te kunnen verbinden met DB
        ParseInstallation.getCurrentInstallation().saveInBackground();
        binding.alreadyHaveAccountText.setOnClickListener { this.HaveAcc(); }
        binding.btnRegister.setOnClickListener {
            try {
                this.Registeren();
            } catch (ex: Exception) {
            }
        }

        // init de dbAdapter met huidige context in constr.
        helper = DbAdapter(this)
        AuthService = AuthService()

        // set een onclick event listerer voor de 'selecteer foto' button
        binding.fotoSelector.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0);
        }

        // MUSICA MAESTROOOO
        Log.e("Service", "Musicservice wordt gestart...")
        // hier maken we service effectief aan (onCreate method)
        serviceItent = Intent(binding.root.context, MusicServiceSSA::class.java);
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
           binding.fotoSelector.background = bitmapDrawable;
           binding.fotoSelector.text = "";
       }
    }


    // effectief registreren naar parseDB
    public fun Registeren() {
        val regName = binding.inptName.text.toString()
        val regEmail =  binding.inptEmail.text.toString()
        val regPasswrd =  binding.inptPassword.text.toString()

        AuthService!!.Register(regName, regEmail, regPasswrd) {
            // passed though the oncompletedlistener from service
            // if true then the user has been succesfully registered
            result -> when(result){
                "SUCCESS" -> uploadImageToFirebaseStorage()
                else -> binding.tvError.text = result
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
                    Log.e("USERCREATION", "SAVING USER TO FIREBASE DATABASE STORAGE...")
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

            // wanneer registratie succesvol, slaag de user ook op in eigen database
             helper!!.insertData(regName,profileImageUrl,user.uid)

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

    // STANDARD FUNCT TO IMPLEMENT ON EACH VIEW
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // item.itemId = de id dat aan de items zijn gelinkt in het menu dat je hebt aangemaakt
        when (item?.itemId) {
            // SWITCH CASE
            R.id.sound_switch -> {
                Log.e("menubar", "clicked sound switch!")

            }
        }
        return super.onOptionsItemSelected(item);
    }



    // create the overal options menu ( just the layout )
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu_login_reg, menu);
        val toggleService = menu?.findItem(R.id.sound_switch);
        val switch = toggleService?.actionView as Switch
        //check of de music reeds gestart is zet de initiele switch naargelang de uitkomt
        if(applicationContext.isMyServiceRunning(MusicServiceSSA::class.java))
            switch.setChecked(true);
        switch.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                // Start or stop your Service
                var musicService = Intent(applicationContext, MusicServiceSSA::class.java)
                if(!isChecked) {
                    stopService(musicService);
                } else {
                    startService(musicService);
                }
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

}



class User(val uid:String, val name:String, val profileImageUrl: String)
