package com.secret.santa.views


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.secret.santa.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_account_overview.*

class AccountMainSSA() : AppCompatActivity() {


    @Override
    // Algemene Oncreate Functie om layout aan te roepen
    override fun onCreate(savedInstanceState: Bundle?) {
        // overerven van param
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Account"
        // de juiste config. aanroepen om te kunnen verbinden met DB
        // het definiëren van de layout keuze
        setContentView(R.layout.activity_account_overview);
        btnAccountFavoList.setOnClickListener { GoToAccountFavoList() }
        btnAccountPersonal.setOnClickListener { GoToAccountDetails() }
        Picasso.get().load(FirebaseAuth.getInstance().currentUser?.photoUrl).into(imgProfileAccount);

    }

    private fun GoToAccountDetails() {    }

    private fun GoToAccountFavoList() {
        val intent = Intent(this, AccountFavoListSSA::class.java)
        startActivity(intent);
    }

    // STANDARD FUNCT TO IMPLEMENT ON EACH VIEW
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // item.itemId = de id dat aan de items zijn gelinkt in het menu dat je hebt aangemaakt
        when (item?.itemId){
            // SWITCH CASE
            R.id.menuSignOut -> {
                FirebaseAuth.getInstance().signOut();
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
