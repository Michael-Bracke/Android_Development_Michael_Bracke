package com.example.secret_santa_app.views


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.secret_santa_app.R
import com.parse.ParseInstallation
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_group_overview.*
import kotlinx.android.synthetic.main.toolbar_actionbar_custom.*


class GroupOverviewSSA() : AppCompatActivity() {


    @Override
    // Algemene Oncreate Functie om layout aan te roepen
    override fun onCreate(savedInstanceState: Bundle?) {
        // overerven van param
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);

        // de juiste config. aanroepen om te kunnen verbinden met DB
        ParseInstallation.getCurrentInstallation().saveInBackground();
        // het definiëren van de layout keuze
        setContentView(R.layout.activity_group_overview)
        // set click listener event on button
        btnGroupCreation.setOnClickListener {
            GroupCreationV();
        }
        btnGroupCode.setOnClickListener { ToCodeView() }
        // check the groups for the user
        CheckGroupForUser();
        btnHomeToolbar.setOnClickListener { setHomeActivity() }
        btnAccountToolbar.setOnClickListener { setAccountActivity(); }
        btnGroupToolbar.setOnClickListener { setGroupActivity() }


    }

    private fun ToCodeView(){
        val intent = Intent(this, GroupCodeFillSSA::class.java);
        startActivity(intent);
    }
    private fun GroupCreationV() {
        val intent = Intent(this, GroupCreationSSA::class.java);
        startActivity(intent);
    }


    private fun CheckGroupForUser() {
        layoutList.removeAllViews();
        // Creates a new ParseQuery object to help us fetch group objects
        val query = ParseQuery.getQuery<ParseObject>("Group");
        // specify the userIDs column to check base don current user
        query.whereEqualTo("UserIDs", ParseUser.getCurrentUser().objectId)
        try {
            // get the results (list of parseObjects)
            val results = query.find();
            for (result in results) {
                // gebruik resultaat om view op te vullen & voeg toe aan layoutlist
                AddView(result);
            }
        } catch (e : Exception) {
            e.printStackTrace();
        }
    }


    // creeër('inflate') de view dat we hebben gemaakt onder layou.row_add_group
    // deze linear layout bevat elementen die allemaal een ID hebben
    // hier roepen we deze elem op, en wijzen we values toe om de view op te bouwen met data
    // we voegen deze view dan toe aan de LayoutList op deze activity

    private fun AddView(groupResult: ParseObject){
        var groupView = layoutInflater.inflate(R.layout.row_add_group, null, false);
        val btnDetails = groupView.findViewById<Button>(R.id.btnGroupDetail);
        btnDetails.setOnClickListener { GoToGroupDetail(groupResult.objectId); }
        btnDetails.text = (groupResult.getString("Name"));
        val imageClose = groupView.findViewById<ImageView>(R.id.imgCross);
        imageClose.setOnClickListener{DeleteGroup(groupResult.objectId);}
        layoutList.addView(groupView);
    }


    private fun DeleteGroup(id : String){
        val query = ParseQuery.getQuery<ParseObject>("Group")
        // Retrieve the object by id
        query.whereEqualTo("objectId", id)
        var results = query.find();
        results.forEach(){
            it.deleteInBackground()
            CheckGroupForUser();
        }
    }

    private fun GoToGroupDetail(id : String){
        val intent = Intent(this, GroupDetailSSA::class.java)
        val b = Bundle()
        b.putString("groupID", id) //Your id
        intent.putExtras(b) //Put your id to your next Intent
        startActivity(intent)
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
