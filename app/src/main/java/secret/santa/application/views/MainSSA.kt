package com.secret.santa.views


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.secret.santa.R
import com.secret.santa.databinding.ActivityMainOverviewBinding


class MainSSA() : AppCompatActivity() {

    var serviceItent = Intent();
    private lateinit var binding: ActivityMainOverviewBinding

    @Override
    // Algemene Oncreate Functie om layout aan te roepen
    override fun onCreate(savedInstanceState: Bundle?) {
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
            R.id.lang_selector -> {
                val intent = Intent(this, LanguageSSA::class.java)
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
