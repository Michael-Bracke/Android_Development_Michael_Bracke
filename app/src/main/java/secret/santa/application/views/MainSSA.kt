package com.secret.santa.views


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.secret.santa.R
import kotlinx.android.synthetic.main.activity_main_overview.*
import secret.santa.application.services.MusicServiceSSA


class MainSSA() : AppCompatActivity() {

    var serviceItent = Intent();

    @Override
    // Algemene Oncreate Functie om layout aan te roepen
    override fun onCreate(savedInstanceState: Bundle?) {
        // overerven van param
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Homepagina"
        // het definiëren van de layout keuze
        setContentView(R.layout.activity_main_overview)
        // de juiste config. aanroepen om te kunnen verbinden met DB
        val uid = FirebaseAuth.getInstance().uid ?: "";
        val name = FirebaseDatabase.getInstance(getString(R.string.database_instance)).getReference("users").child(uid).child("name");
        welcomeText.text = "Welkom, " + FirebaseAuth.getInstance().currentUser?.displayName + "!"
        btnAccount.setOnClickListener{ GoToAccountDetail();}
        btnGroupOverview.setOnClickListener{ GoToGroupDetail();}
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

            R.id.menuSound -> {



            }

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
        menu?.getItem(1)?.setOnMenuItemClickListener {
            // MUSICA MAESTROOOO
            // hier roepen we service aan en starten we hem
            serviceItent = Intent(applicationContext, MusicServiceSSA::class.java);
            menu.getItem(0).setVisible(true)
            it.setVisible(false)
            startService(serviceItent)
           true
        }
        menu?.getItem(0)?.setOnMenuItemClickListener {
            // MUSICA MAESTROOOO
            // hier roepen we service aan en starten we hem
            serviceItent = Intent(applicationContext, MusicServiceSSA::class.java);
            menu.getItem(1).setVisible(true)
            it.setVisible(false)
            stopService(serviceItent)
            true
        }

        return super.onCreateOptionsMenu(menu);
    }


}
