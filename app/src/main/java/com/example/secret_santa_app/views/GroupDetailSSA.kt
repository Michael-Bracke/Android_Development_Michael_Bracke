package com.example.secret_santa_app.views


import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.secret_santa_app.R
import com.parse.ParseInstallation
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_group_details.*
import kotlinx.android.synthetic.main.activity_group_overview.*
import kotlinx.android.synthetic.main.activity_group_overview.layoutList
import kotlinx.android.synthetic.main.toolbar_actionbar_custom.*
import org.json.JSONArray
import java.nio.file.Files.list
import java.util.Collections.list


class GroupDetailSSA() : AppCompatActivity() {


    @Override
    // Algemene Oncreate Functie om layout aan te roepen
    override fun onCreate(savedInstanceState: Bundle?) {
        // overerven van param
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        // de juiste config. aanroepen om te kunnen verbinden met DB
        ParseInstallation.getCurrentInstallation().saveInBackground();
        // het definiëren van de layout keuze
        setContentView(R.layout.activity_group_details)
        // get the extras die mee werden verzonden als deze activity werd opgeropen
        val b = intent.extras
        var value = "" // or other values
        if (b != null) value = b.getString("groupID").toString()
        CheckGroupDetails(value);
        btnShareGroup.setOnClickListener { ShareLink(value) }
        btnHomeToolbar.setOnClickListener { setHomeActivity() }
        btnAccountToolbar.setOnClickListener { setAccountActivity(); }
        btnGroupToolbar.setOnClickListener { setGroupActivity() }

    }

    private fun ShareLink(groupID: String) {
         val intent = Intent(Intent.ACTION_SEND);
         intent.setType("text/plain")
        // onderwerp van de mail bv.
         val shareBodyTitle = "Graag nodig ik je uit voor mijn secret santa groep!"
        // inhoud van de mail
         val shareBodyInner = "Hier is jouw unieke code : $groupID, meld je aan in de secret santa app: https://www.hierkomtdelinklater.be (je kan de code gebruiken om jezelf aan te sluiten bij de groep in de app!"

        intent.putExtra(Intent.EXTRA_SUBJECT, shareBodyTitle);
        intent.putExtra(Intent.EXTRA_TEXT, shareBodyInner)

        startActivity(Intent.createChooser(intent, "Mensen uitnodigen"))
    }

    private fun CheckGroupDetails(id: String) {
        layoutList.removeAllViews();
        // Creates a new ParseQuery object to help us fetch group objects
        val query = ParseQuery.getQuery<ParseObject>("Group");
        // specify the userIDs column to check base don current user
        query.whereEqualTo("objectId", id)
        try {
            // get the results (list of parseObjects)
            val results = query.find();
            for (result in results) {
                tvGroupName.text = result.get("Name").toString();
                // gebruik resultaat om view op te vullen & voeg toe aan layoutlist
                // in dit geval gaan we dus vooral de JsonArray ndoig hebben
                // verkrijg de array
                var str = arrayOf(result.get("UserIDs"))
                // trim de array ( haken weg om deftig te kunnen splitten )
                val trimmedString =
                    str[0].toString().replace("[", "").replace("]", "").replace(" ", "");
                Log.d("main", "$trimmedString");
                val strSplit = trimmedString.split(',');
                for (i in 0..strSplit.count()) {
                    printUser(strSplit[i]);
                }
            }
        } catch (e: Exception) {
            e.printStackTrace();
        }
    }

    private fun printUser(objectIdUser: String) {
        val query = ParseUser.getQuery()
        Log.d("main", "$objectIdUser");
        query.whereEqualTo("objectId", objectIdUser)
        query.findInBackground { users, e ->
            if (e == null) {
                // The query was successful, returns the users that matches
                // the criterias.
                for (user in users) {
                    AddView(user);
                }
            } else {
                // Something went wrong.
            }
        }
    }


    // creeër('inflate') de view dat we hebben gemaakt onder layou.row_add_user
    // deze linear layout bevat elementen die allemaal een ID hebben
    // hier roepen we deze elem op, en wijzen we values toe om de view op te bouwen met data
    // we voegen deze view dan toe aan de LayoutList op deze activity

    private fun AddView(userResult: ParseUser) {
        var userView = layoutInflater.inflate(R.layout.row_add_user, null, false);
        val editText = userView.findViewById<TextView>(R.id.tvText);
        editText.text = (userResult.getString("Naam"));
        //val imageClose = groupView.findViewById<ImageView>(R.id.imgCross);
        //imageClose.setOnClickListener{DeleteGroup(user.objectId);}
        layoutList.addView(userView);
    }


    private fun DeleteGroup(id: String) {
        val query = ParseQuery.getQuery<ParseObject>("Group")
        // Retrieve the object by id
        query.whereEqualTo("objectId", id)
        var results = query.find();
        results.forEach() {
            it.deleteInBackground()
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

