package com.secret.santa.views


import android.content.Intent
import android.os.Bundle
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

import com.parse.ParseUser
import com.secret.santa.R
import com.secret.santa.databinding.ActivityAccountOverviewBinding
import com.secret.santa.databinding.ActivityGroupListOverviewBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

import secret.santa.application.models.Group
import secret.santa.application.models.User


class GroupOverviewSSA() : AppCompatActivity() {

    private lateinit var binding: ActivityGroupListOverviewBinding


    @Override
    // Algemene Oncreate Functie om layout aan te roepen
    override fun onCreate(savedInstanceState: Bundle?) {
        // overerven van param
        super.onCreate(savedInstanceState)
        binding = ActivityGroupListOverviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Groepen"
        binding.tvGroupOverViewHeader.text = "Groepen waar jij lid van bent"

        // nieuwe groep toevoegen
        binding.btnAddGroup.setOnClickListener {
            val intent = Intent(this, GroupCreationSSA::class.java)
            startActivity(intent)
        }
        // code verzilveren
        binding.btnHaveCode.setOnClickListener {
            val intent = Intent(this, GroupCodeFillSSA::class.java)
            startActivity(intent)
        }


        // het definiëren van de adapter
        val adapter = GroupAdapter<GroupieViewHolder>()


        fetchGroups()

    }

    companion object {
        val GROUP_ID = "GROUP_ID"
        val GROUP_NAME = "GROUP_NAME"
        val GROUP = "GROUP_"
        val TAG = "GROUP_OVERVIEW"
        val GROUP_INVITE_ID = "GROUP_INVITE_ID"
    }
    private fun fetchGroups() {
       val ref =  FirebaseDatabase.getInstance(getString(R.string.database_instance)).getReference("/groups")
       ref.addListenerForSingleValueEvent(object: ValueEventListener{
           override fun onDataChange(snapshot: DataSnapshot) {
               val adapter = GroupAdapter<GroupieViewHolder>()
               // Vekrijg alle groepen die op de node zitten
               snapshot.children.forEach{
                   // itereer over elke groep dat gevonden is
                   // en cast deze naar een 'group' model
                   val group = it.getValue(Group::class.java)
                   // vul onze adapter met deze data
                   if(group != null){
                       Log.d(TAG, "current group searching in.... ${group.Name}")
                       val ref = FirebaseDatabase.getInstance(getString(R.string.database_instance)).getReference("/usergroups/${group.Id}")
                       Log.d(TAG, "REFRENCE -> $ref" )
                       val query = ref.orderByChild("Uid").equalTo(FirebaseAuth.getInstance().uid)
                       Log.d(TAG, "QUERY -> $query" )
                       query.addValueEventListener(object:ValueEventListener{
                           override fun onCancelled(error: DatabaseError) {
                               TODO("Not yet implemented")
                           }
                           override fun onDataChange(snapshot: DataSnapshot) {
                               if(snapshot.children.count() == 0){
                                   binding.tvGroupCount.text = "Je bent nog niet aangesloten bij een groep. Maak zelf een groep of wacht tot je wordt uitgenodigd!"
                               }
                              snapshot.children.forEach{
                                 adapter.add(Groupitem(group))
                                  binding.tvGroupCount.text = ""
                              }

                           }
                       })

                       /*
                       val userIds = group.UserIDs
                       if(userIds.contains(FirebaseAuth.getInstance().uid!!, ignoreCase = true)){
                           adapter.add(Groupitem(group))

                       }
                       */


                   }
               }
               adapter.setOnItemClickListener{
                   // item refereerd naar het item dat effectief gerenderd wordt
                   item, view->
                   // cast the item to the item we want to use
                   val groupitem = item as Groupitem
                   // create the intent to start the next activity
                   val intent = Intent(view.context, GroupDetailSSA::class.java)
                   // send extra params
                   intent.putExtra(GROUP_ID, item.group.Id)
                   intent.putExtra(GROUP_NAME, item.group.Name)
                   intent.putExtra(GROUP_INVITE_ID, item.group.InviteId)
                   // sending whole object
                   // om een heel object te kunnen versturen moet dit
                   // 'GEPARSED' worden naar correct weergave
                   // dit kan door de PARCELIZE EXTENSIE
                   // android expertimental -> true!! & annotation voorzien in klasse
                   // zo worden de method's authomatisch geimplementeerd
                   // en kan je dus als zodoende de klasse gebruiken om door sturen
                   intent.putExtra(GROUP, item.group)

                   startActivity(intent)


               }
               // maak het visueel zichtbaar door deze adapter ook te binden
               // met de recyclerview
               binding.recyclerViewGroupOverview.adapter = adapter

           }
           override fun onCancelled(error: DatabaseError) { }

       })
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


class Groupitem(val group: Group): Item<GroupieViewHolder>(){
    override fun getLayout(): Int {
        // define de actuele row dat je gaat gebruiken om de data in te steken
        return R.layout.group_row
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        // wordt opgeroepen in onze lijst van groepobjecten
        // voor ieder groep object apart

        val title = viewHolder.itemView.findViewById<TextView>(R.id.tvGroupOverviewDetail)
        title.text = group.Name
    }

}




