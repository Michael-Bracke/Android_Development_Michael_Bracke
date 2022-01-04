package com.secret.santa.views


import DownloadWorker
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.secret.santa.R
import com.secret.santa.databinding.ActivityMainOverviewBinding
import secret.santa.application.interfaces.Listener
import secret.santa.application.services.MusicServiceSSA
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import secret.santa.application.models.Group
import java.io.InputStream
import java.lang.Exception
import java.net.URL


class MainSSA() : AppCompatActivity() {

    var serviceItent = Intent();
    private lateinit var binding: ActivityMainOverviewBinding

    @Override
    // Algemene Oncreate Functie om layout aan te roepen
    override fun onCreate(savedInstanceState: Bundle?) {


        val policy: StrictMode.ThreadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        // overerven van param
        super.onCreate(savedInstanceState)
        binding = ActivityMainOverviewBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.title = "Homepagina"
        // het definiëren van de layout keuze
        // de juiste config. aanroepen om te kunnen verbinden met DB
        val uid = FirebaseAuth.getInstance().uid ?: "";
        val name = FirebaseDatabase.getInstance(getString(R.string.database_instance)).getReference("users").child(uid).child("name");
        binding.welcomeText.text = "Welkom, " + FirebaseAuth.getInstance().currentUser?.displayName + "!"
        binding.btnAccount.setOnClickListener{ GoToAccountDetail();}
        binding.btnGroupOverview.setOnClickListener{ GoToGroupDetail();}

        // Haal afbeelding op, aangezien er async methods worden aangeroepen
        // gebruik je hier callbacks om de profImageUrl mee te geven aan de
        // Downloadworker, deze downloadworker voorziet via threading de correcte bitmap

        fetchProfImageUrl { result ->
            DownloadWorker(binding.root.context)
                .downloadImage(result!!, object: Listener<Bitmap?> {
                    override fun DownloadComplete(arg: Bitmap?) {
                        Log.e("RUNNABLE", "" + arg)
                        binding.imgAvatar.setImageBitmap(arg)
                    }
                })
         }


    }

    private fun fetchProfImageUrl(callback : (String?) -> Unit) : String? {
        var imgUrl : String? = ""
        val ref =  FirebaseDatabase.getInstance(getString(R.string.database_instance)).getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    // itereer over elke user dat gevonden is
                    var user = it.getValue(secret.santa.application.models.User::class.java)
                    if(user != null){
                        Log.e("User", ""+ user!!.ProfileImageUrl)
                        val userRef = FirebaseDatabase.getInstance(getString(R.string.database_instance)).getReference("/users/"+it.key)
                        val query = userRef.orderByChild("uid").equalTo(FirebaseAuth.getInstance().uid)
                        query.addListenerForSingleValueEvent(object: ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                // hier weten we dat ALS we hier in komen, we de correcte gefilterde user hebben
                                imgUrl = user?.ProfileImageUrl




                                if(imgUrl != null)
                                    callback(imgUrl)
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })
                }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        });
        return imgUrl;
    }

    // veranderen van actieve layout view
    private fun GoToGroupDetail() {
        val intent = Intent(this, GroupOverviewSSA::class.java)
        startActivity(intent);
    }

    private fun GoToAccountDetail(){
        val intent = Intent(this, AccountMainSSA::class.java)
        startActivity(intent);
    }


    // STANDARD FUNCT TO IMPLEMENT ON EACH VIEW
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // item.itemId = de id dat aan de items zijn gelinkt in het menu dat je hebt aangemaakt
        when (item?.itemId){
            // SWITCH CASE
            R.id.menuSignOut -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, LoginSSA::class.java)
                // deze stap is belangerijk mits dit er voor gaat zorgen dat de user eens hij is ingelogd
                // NIET weer terug gaat naar het regisratieformulier met de 'terug' toets
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            R.id.menuGroup -> {
                val intent = Intent(this, GroupOverviewSSA::class.java)
                startActivity(intent);
            }
            R.id.menuHome -> {
                val intent = Intent(this, MainSSA::class.java)
                startActivity(intent);
            }
        }
      return super.onOptionsItemSelected(item);
    }
    // create the overal options menu ( just the layout )
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


}
