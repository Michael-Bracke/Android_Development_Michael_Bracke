package com.example.secret_santa_app.views


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.secret_santa_app.R
import com.parse.ParseInstallation
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_account_overview.*
import kotlinx.android.synthetic.main.activity_group_code_fill.*
import kotlinx.android.synthetic.main.toolbar_actionbar_custom.*
import org.json.JSONArray

class AccountMainSSA() : AppCompatActivity() {


    @Override
    // Algemene Oncreate Functie om layout aan te roepen
    override fun onCreate(savedInstanceState: Bundle?) {
        // overerven van param
        super.onCreate(savedInstanceState)
        // de juiste config. aanroepen om te kunnen verbinden met DB
        ParseInstallation.getCurrentInstallation().saveInBackground();
        // het definiëren van de layout keuze
        setContentView(R.layout.activity_account_overview);
        btnAccountFavoList.setOnClickListener { GoToAccountFavoList() }
        btnAccountPersonal.setOnClickListener { GoToAccountDetails() }
        btnHomeToolbar.setOnClickListener { setHomeActivity() }
        btnAccountToolbar.setOnClickListener { setAccountActivity(); }
        btnGroupToolbar.setOnClickListener { setGroupActivity() }
    }

    private fun GoToAccountDetails() {    }

    private fun GoToAccountFavoList() {
        val intent = Intent(this, AccountFavoListSSA::class.java)
        startActivity(intent);
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
