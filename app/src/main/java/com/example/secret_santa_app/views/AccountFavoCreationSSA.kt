package com.example.secret_santa_app.views


import android.content.Intent
import android.os.Bundle
import android.text.TextUtils.isEmpty
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.secret_santa_app.R
import com.example.secret_santa_app.models.FavoriteItem
import com.example.secret_santa_app.models.Group
import com.parse.*
import kotlinx.android.synthetic.main.activity_account_favo_creation.*
import kotlinx.android.synthetic.main.activity_group_creation.*
import kotlinx.android.synthetic.main.toolbar_actionbar_custom.*
import org.json.JSONArray


class AccountFavoCreationSSA() : AppCompatActivity() {


    @Override
    // Algemene Oncreate Functie om layout aan te roepen
    override fun onCreate(savedInstanceState: Bundle?) {
        // overerven van param
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        // de juiste config. aanroepen om te kunnen verbinden met DB
        ParseInstallation.getCurrentInstallation().saveInBackground();
        // het definiëren van de layout keuze
        setContentView(R.layout.activity_account_favo_creation);
        btnAddFavoItem.setOnClickListener { CreateFavoItem() }
        btnHomeToolbar.setOnClickListener { setHomeActivity() }
        btnAccountToolbar.setOnClickListener { setAccountActivity(); }
        btnGroupToolbar.setOnClickListener { setGroupActivity() }
    }


    private fun CreateFavoItem() {

        val inptFavName = findViewById<TextView>(R.id.inptFavoName);

        if (!isEmpty(inptFavName.text.toString())) { // check of de input value niet leeg is
            // maak favo item aan
            val favo = FavoriteItem(inptFavName.text.toString(), true, ParseUser.getCurrentUser().objectId, "", "https://www.google.be");
            // functie van class oproepen (eigen maak) om rechtstreeks naar db te kunnen schrijven
            favo.StoreDatabase();
            // verander van layout
            val intent = Intent(this, AccountFavoListSSA::class.java)
            intent.putExtra("scssMsg", true)
            startActivity(intent);
        }
        else {
            inptFavName.setError("Gelieve een item naam in te voeren!")
        }

    }

    // STANDARD FUNCT TO IMPLEMENT ON EACH VIEW
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return true
    }
    private fun setHomeActivity(){
        val intent = Intent(this, MainSSA::class.java)
        startActivity(intent);
    }
    private fun setAccountActivity(){
        val intent = Intent(this, AccountMainSSA::class.java)
        startActivity(intent);
    }
    private fun setGroupActivity(){
        val intent = Intent(this, GroupOverviewSSA::class.java)
        startActivity(intent);
    }


}
