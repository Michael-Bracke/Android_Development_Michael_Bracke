package com.secret.santa.views


import android.content.Intent
import android.os.Bundle
import android.text.TextUtils.isEmpty
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.secret.santa.R
import kotlinx.android.synthetic.main.activity_account_favo_creation.*
import secret.santa.application.models.FavoriteItem
import secret.santa.application.services.MusicServiceSSA


class AccountFavoCreationSSA() : AppCompatActivity() {


    @Override
    // Algemene Oncreate Functie om layout aan te roepen
    override fun onCreate(savedInstanceState: Bundle?) {
        // overerven van param
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Nieuw item toevoegen"
        // de juiste config. aanroepen om te kunnen verbinden met DB
        // het definiëren van de layout keuze
        setContentView(R.layout.activity_account_favo_creation);
        btnAddFavoItem.setOnClickListener { CreateFavoItem() }
    }


    private fun CreateFavoItem() {

        val inptFavName = findViewById<TextView>(R.id.inptFavoName);

        if (!isEmpty(inptFavName.text.toString())) { // check of de input value niet leeg is
            val favoItem = FavoriteItem(
                inptFavName.text.toString(),
                true,
                FirebaseAuth.getInstance().uid,
                "",
                ""
            )
            val ref = FirebaseDatabase.getInstance(getString(R.string.database_instance))
                .getReference("/favoitems").push()
            // push creërt een unieke key op waar deze node zal terechtkomen, deze Id kunnen we telkens later gerbuiken
            // vb -> favoItem
            //         --KEY
            //           --param1
            //           --param2
            //           --param3

            favoItem.id = ref.key
            ref.setValue(favoItem)
                .addOnSuccessListener {
                    val intent = Intent(this, AccountFavoListSSA::class.java)
                    intent.putExtra("scssMsg", true)
                    startActivity(intent);
                }


        } else {
            inptFavName.setError("Gelieve een item naam in te voeren!")
        }

    }

    // STANDARD FUNCT TO IMPLEMENT ON EACH VIEW
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // item.itemId = de id dat aan de items zijn gelinkt in het menu dat je hebt aangemaakt
        when (item?.itemId) {
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
        var serviceItent = Intent();
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
