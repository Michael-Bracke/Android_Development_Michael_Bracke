package com.secret.santa.views


import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.secret.santa.R
import com.secret.santa.databinding.ActivityGroupDetailBinding
import com.secret.santa.databinding.ActivityGroupListOverviewBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import secret.santa.application.models.Group
import secret.santa.application.models.User
import secret.santa.application.views.chat.GroupChatLog


class GroupDetailSSA() : AppCompatActivity() {

    companion object{
        val GROUP = "GROUP_"
    }
    val adapter = GroupAdapter<GroupieViewHolder>()
    private lateinit var binding: ActivityGroupDetailBinding

    @Override
    // Algemene Oncreate Functie om layout aan te roepen
    override fun onCreate(savedInstanceState: Bundle?) {
        // overerven van param
        super.onCreate(savedInstanceState)
        binding = ActivityGroupDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)        // het the correct groupId from the previous view
        // ( waar user groep selecteerd)
        val group = intent.getParcelableExtra<Group>(GroupOverviewSSA.GROUP)
        if(group != null) {
            val groupId = group.Id
            supportActionBar?.title = group.Name;
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            fetchUsersForGroup(group);
            binding.btnShareGroup.setOnClickListener {
                val inviteId = intent.getStringExtra(GroupOverviewSSA.GROUP_INVITE_ID);
                Log.d("SHARING",inviteId.toString())
                val intent = Intent(android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Ssst.. jij bent uitgenodigd voor mijn secret santa groep!");
                val subjectString = "Hier is jouw unieke code : "+ inviteId.toString() +" om te verzilveren in de app! "
                intent.putExtra(Intent.EXTRA_TEXT, subjectString);
                startActivity(Intent.createChooser(intent, "Hoe wil iemand uitnodigen?"));
            }
        }
        // het defienieren van de adapter





        binding.recyclerViewGroupDetailOverview.adapter = adapter
        val btn = findViewById<Button>(R.id.btnGroupChat)
        btn.setOnClickListener {
            val intent = Intent(this, GroupChatLog::class.java);
            // eventueel extra param meegeven?
            intent.putExtra(GROUP, group)
            startActivity(intent);
        }
    }

    private fun fetchUsersForGroup(group:Group) {
        // maak dat je op de juiste ref zit
        val ref = FirebaseDatabase.getInstance(getString(R.string.database_instance)).getReference("/usergroups/${group.Id}")
        // haal nu simpelweg de juiste users weer binnen ;)
        val query = ref.orderByChild("Uid");
        query.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach{
                    var user = it.getValue(User::class.java)
                    if(user != null)
                       adapter.add(GroupDetailItem(user))
                }
            }

        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // item.itemId = de id dat aan de items zijn gelinkt in het menu dat je hebt aangemaakt
        when (item?.itemId) {
            // SWITCH CASE
            R.id.menuSignOut -> {
                FirebaseAuth.getInstance().signOut()
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


class GroupDetailItem(val user: User) : Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.row_add_user
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.tvText).text = user.Name;
    }

}








