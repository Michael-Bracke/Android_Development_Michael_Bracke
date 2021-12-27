package com.secret.santa.views


import android.content.Intent
import android.os.Bundle
import android.text.TextUtils.isEmpty
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.secret.santa.R

import secret.santa.application.models.Group
import secret.santa.application.models.User
import java.util.*
import kotlin.random.Random
import kotlin.random.Random.Default.nextInt


class GroupCreationSSA() : AppCompatActivity() {

    private val TAG = "GroupCreation"

    @Override
    // Algemene Oncreate Functie om layout aan te roepen
    override fun onCreate(savedInstanceState: Bundle?) {
        // overerven van param
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Groep aanmaken"
        // de juiste config. aanroepen om te kunnen verbinden met DB

        // het definiëren van de layout keuze
        setContentView(R.layout.activity_group_creation);
        //TODO  btnJoinGroup.setOnClickListener { CreateGroup() }


    }

    private fun GroupCreationV() {
        val intent = Intent(this, GroupCreationSSA::class.java);
        startActivity(intent);
    }

    private fun CreateGroup() {
        val randomInviteNumber = Random.nextInt(1000,9999).toString()
        val randomUserGroupID = Random.nextInt(4000000,999999999).toString()
        val inptGroupName = findViewById<TextView>(R.id.inptGroupCode);

        if (!isEmpty(inptGroupName.text.toString())) { // check of de input value niet leeg is
            // create nieuwe array om de uID in te storen
            // maak group aan
            val group = Group(
                inptGroupName.text.toString(),
                true,
                randomUserGroupID,
                FirebaseAuth.getInstance().uid,
                randomInviteNumber
            )
            // write to db
            saveInDatase(group);
        }
        else {
            inptGroupName.setError("Gelieve een groepnaam in te voeren!")
        }

    }

    private fun saveInDatase(group: Group){

        val groupDbRef = FirebaseDatabase.getInstance(getString(R.string.database_instance)).getReference("/groups").push()
        val userGroupRef = FirebaseDatabase.getInstance(getString(R.string.database_instance)).getReference("/usergroups/${groupDbRef.key}").push()
        group.Id = groupDbRef.key;
        group.UserGroupId = userGroupRef.key

        groupDbRef.setValue(group)

        val currentUserFromFireBaseAuth = FirebaseAuth.getInstance().currentUser;
        if(currentUserFromFireBaseAuth != null) {
            // fill use model based on logged user
            val user = User(
                currentUserFromFireBaseAuth.displayName,
                currentUserFromFireBaseAuth.photoUrl.toString(),
                currentUserFromFireBaseAuth.uid
            )
            userGroupRef.setValue(user).addOnSuccessListener {
                Log.d(TAG, "user is aangemaakt -> ${user.Uid}")
                val intent = Intent(this, GroupOverviewSSA::class.java)
                // deze stap is belangerijk mits dit er voor gaat zorgen dat de user eens hij is ingelogd
                // NIET weer terug gaat naar het regisratieformulier met de 'terug' toets
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("scssMsg", true)
                startActivity(intent);
            }
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



}
