package com.secret.santa.views


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.secret.santa.R
import com.parse.ParseInstallation
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser

import org.json.JSONArray
import secret.santa.application.models.Group
import secret.santa.application.models.User

class GroupCodeFillSSA() : AppCompatActivity() {


    companion object {
        val TAG = "GROUPCODE"
    }

    @Override
    // Algemene Oncreate Functie om layout aan te roepen
    override fun onCreate(savedInstanceState: Bundle?) {
        // overerven van param
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Groepcode invullen"
        // de juiste config. aanroepen om te kunnen verbinden met DB

        // het definiëren van de layout keuze
        setContentView(R.layout.activity_group_code_fill);

        //TODO btnJoinGroup.setOnClickListener { JoinGroup(); }


    }


    private fun JoinGroup() {
        /* //TODO
        val text = inptGroupCode.text;
        if (!text.isEmpty()) {
            val ref = FirebaseDatabase.getInstance(getString(R.string.database_instance))
                .getReference("/groups")
            val query = ref.orderByChild("InviteId").equalTo(text.toString())

            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // check wether data is found
                    for (childsnapshot in snapshot.children) {
                        // found group
                        Log.d(TAG,
                            "FOUND GROUP WITH CORRECT UID" + childsnapshot.getValue(Group::class.java)
                                .toString()
                        )
                        val group = childsnapshot.getValue(Group::class.java)
                        if (group != null) {
                            val userGroupRef = FirebaseDatabase.getInstance(getString(R.string.database_instance)).getReference("/usergroups/${group.Id}").push()
                            val currentUserFromFireBaseAuth = FirebaseAuth.getInstance().currentUser;
                            if(currentUserFromFireBaseAuth != null) {
                                val user = User(
                                    currentUserFromFireBaseAuth.displayName,
                                    currentUserFromFireBaseAuth.photoUrl.toString(),
                                    currentUserFromFireBaseAuth.uid
                                )
                                userGroupRef.setValue(user).addOnSuccessListener {
                                    Log.d(TAG, "user is toegevaagd aan groep lijst-> ${user.Uid}")
                                    val intent = Intent(this@GroupCodeFillSSA, GroupOverviewSSA::class.java)
                                    // deze stap is belangerijk mits dit er voor gaat zorgen dat de user eens hij is ingelogd
                                    // NIET weer terug gaat naar het regisratieformulier met de 'terug' toets
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("scssMsg", true)
                                    startActivity(intent);
                                }
                            }
                            } else
                                Log.d(TAG, "FOUND GROUP AND USER DOES BELONG TO THE CURRENT GROUP")

                        }

                    }



                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        } else {
            inptGroupCode.setError("Gelieve een code in tegeven")
        }
   */
    }


    // STANDARD FUNCT TO IMPLEMENT ON EACH VIEW
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // item.itemId = de id dat aan de items zijn gelinkt in het menu dat je hebt aangemaakt
        when (item?.itemId) {
            // SWITCH CASE
            R.id.menuSignOut -> {
                ParseUser.logOut();
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
