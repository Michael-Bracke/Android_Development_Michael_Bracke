package secret.santa.application.views.chat

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.secret.santa.R
import com.secret.santa.databinding.ActivityAccountFavoCreationBinding
import com.secret.santa.databinding.ActivityGroupChatLogBinding
import com.secret.santa.views.GroupDetailSSA
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import secret.santa.application.models.ChatMessage
import secret.santa.application.models.Group

class GroupChatLog() : AppCompatActivity() {

    // STATISCHE CONTENT DAT VAN OVERAL KAN OPGEVRAAGD WORDEN
    companion object{
        // handig als prefix voor logging
        val TAG = "Chatlog"
    }
    val adapter = GroupAdapter<GroupieViewHolder>()
    private lateinit var binding: ActivityGroupChatLogBinding

    @Override
    // Algemene Oncreate Functie om layout aan te roepen
    override fun onCreate(savedInstanceState: Bundle?) {
        // overerven van param
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_chat_log)
        binding = ActivityGroupChatLogBinding.inflate(layoutInflater)
        // get group data
        val group = intent.getParcelableExtra<Group>(GroupDetailSSA.GROUP)

        if(group != null){
            supportActionBar?.title = group.Name + " - CHAT"
            binding.btnSendMessage.setOnClickListener {
                performSendMessage(group);
            }
            listenForMessages(group)
        }

        // het definiëren de adapter
        binding.recyclerViewGroupDetailChatLog.adapter = adapter


        //fetchGroupInformation()


    }

    private fun performSendMessage(group:Group) {
      val ref =   FirebaseDatabase.getInstance(getString(R.string.database_instance)).getReference("/group-messages").push()
        // push zorgt ervoor dat deze iedere keer een unique id krijgt

        if(!binding.edSendMessage.text.isEmpty()) // controle op leeg textveld
            else
            binding.edSendMessage.setError("Gelieve een bericht in te voeren!")

        val chatMessage = ChatMessage(ref.key, binding.edSendMessage.text.toString(), FirebaseAuth.getInstance().uid,group.Id, System.currentTimeMillis())
      ref.setValue(chatMessage)
          .addOnSuccessListener {
              Log.d(TAG, "SUCCESVOL OPGESLAGEN");
          }



    }

    private fun listenForMessages(group:Group){
        val ref = FirebaseDatabase.getInstance(getString(R.string.database_instance)).getReference("/group-messages")
        ref.addChildEventListener(object : ChildEventListener{

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(ChatMessage::class.java)
                if(message != null) {
                    if(message.GroupID == group.Id) {
                        if (message.UserID == FirebaseAuth.getInstance().uid) {
                            adapter.add(ChatItemFrom(message))
                        } else {
                            adapter.add(ChatItemTo(message))
                        }
                    }
                }
            }


            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onCancelled(error: DatabaseError) {            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {            }



        })
    }
}

class ChatItemFrom(val chatMessage: ChatMessage) : Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.row_chat_add_message_self
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.messageField).text = chatMessage.GetText()
    }

}

class ChatItemTo(val chatMessage: ChatMessage) : Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.row_chat_add_message_other
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.messageField).text = chatMessage.GetText()
    }

}
/*
    private fun fetchGroupInformation() {
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
                   if(group != null)
                      adapter.add(Groupitem(group))
               }

               // maak het visueel zichtbaar door deze adapter ook te binden
               // met de recyclerview
               recyclerView_GroupOverview.adapter = adapter
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


class GroupDetailItem(val group: Group): Item<GroupieViewHolder>(){
    override fun getLayout(): Int {
        // define de actuele row dat je gaat gebruiken om de data in te steken
        return R.layout.row_add_group
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        // wordt opgeroepen in onze lijst van groepobjecten
        // voor ieder groep object apart
        viewHolder.itemView.btnGroupDetail.text = group.Name
    }
    */


