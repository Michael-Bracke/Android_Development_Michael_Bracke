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
import kotlinx.android.synthetic.main.activity_group_code_fill.*
import kotlinx.android.synthetic.main.toolbar_actionbar_custom.*
import org.json.JSONArray

class GroupCodeFillSSA() : AppCompatActivity() {


    @Override
    // Algemene Oncreate Functie om layout aan te roepen
    override fun onCreate(savedInstanceState: Bundle?) {
        // overerven van param
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        // de juiste config. aanroepen om te kunnen verbinden met DB
        ParseInstallation.getCurrentInstallation().saveInBackground();
        // het definiëren van de layout keuze
        setContentView(R.layout.activity_group_code_fill);
        btnJoinGroup.setOnClickListener { JoinGroup(); }
        btnHomeToolbar.setOnClickListener { setHomeActivity() }
        btnAccountToolbar.setOnClickListener { setAccountActivity(); }
        btnGroupToolbar.setOnClickListener { setGroupActivity() }


    }

    private fun JoinGroup() {

        if(!inptGroupCode.text.isEmpty()){
            val query = ParseQuery.getQuery<ParseObject>("Group")
            // Retrieve the object by id
            Log.d("group", "${inptGroupCode.text.toString()}")
            query.getInBackground(
                inptGroupCode.text.toString()
            ) { entity, e ->
                if (e == null) {
                    var oldValues = entity.get("UserIDs");
                    val jsonArray = JSONArray();
                    Log.d("group", "$oldValues")
                    jsonArray.put(oldValues).put(ParseUser.getCurrentUser().objectId)
                    Log.d("group", "$oldValues")
                    entity.put("UserIDs", jsonArray)
                    // All other fields will remain the same
                    entity.saveInBackground()
                    val intent = Intent(this, GroupOverviewSSA::class.java);
                    startActivity(intent);
                } else {
                    Log.d("group", "${e.message}")
                }
            }
        } else {
            inptGroupCode.setError("Gelieve een code in tegeven")
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
