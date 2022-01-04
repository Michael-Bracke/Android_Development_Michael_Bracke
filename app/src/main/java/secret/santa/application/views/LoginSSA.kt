package com.secret.santa.views

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.parse.ParseUser
import com.secret.santa.R
import com.secret.santa.databinding.ActivityLoginBinding
import com.secret.santa.databinding.ActivityMainOverviewBinding
import secret.santa.application.SQLite.DbAdapter
import secret.santa.application.services.MusicServiceSSA
import secret.santa.application.extensions.isMyServiceRunning

class LoginSSA : AppCompatActivity() {

    companion object{
        val USER_NAME = "USER_NAME"
    }

    private lateinit var binding: ActivityLoginBinding
    var helper: DbAdapter? = null


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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        helper = DbAdapter(this)
        // change to the activity login view
        setContentView(binding.root)
        // create on click event for the login btn
        binding.textRegistreer.setOnClickListener { RegView(); }
        binding.btnInloggen.setOnClickListener {  Login(); }

        // get data from db and log it
        Log.e("DATA", ""+helper!!.data)
        
        // MUSICA MAESTROOOO
        Log.e("Service", "Musicservice wordt gestart...")
        val serviceItent = Intent(binding.root.context, MusicServiceSSA::class.java);
        startService(serviceItent);
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
