package com.secret.santa.views


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.secret.santa.R
import com.parse.ParseInstallation
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser
import com.secret.santa.databinding.ActivityAccountFavoCreationBinding
import com.secret.santa.databinding.ActivityAccountFavoListBinding
import com.secret.santa.databinding.ActivityAccountFavoListFragmentsBinding
import com.secret.santa.databinding.ActivityGroupCodeFillBinding
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import secret.santa.application.models.FavoriteItem
import secret.santa.application.views.favo.FavoriteFragment
import kotlin.concurrent.fixedRateTimer

class AccountFavoListSSA() : AppCompatActivity() {

    // het definiëren van de adapter
    val adapter = GroupAdapter<GroupieViewHolder>()
    private lateinit var binding: ActivityAccountFavoListFragmentsBinding

    @Override
    // Algemene Oncreate Functie om layout aan te roepen
    override fun onCreate(savedInstanceState: Bundle?) {
        // overerven van param
        super.onCreate(savedInstanceState)
        binding = ActivityAccountFavoListFragmentsBinding.inflate(layoutInflater)
        supportActionBar?.title = "Verlanglijstje"
        // het definiëren van de layout keuze
        setContentView(binding.root);
        //Picasso.get().load(FirebaseAuth.getInstance().currentUser?.photoUrl).into(binding.imgProfile);
        binding.btnFavoCreation.setOnClickListener { CreateFavoItem() }
        //CheckFavoItemsForUser()


    }

    private fun CreateFavoItem() {
        val intent = Intent(this, AccountFavoCreationSSA::class.java);
        startActivity(intent);
    }

    /*

    private fun CheckFavoItemsForUser() {

        val ref = FirebaseDatabase.getInstance(getString(R.string.database_instance))
            .getReference("/favoitems");
        val query = ref.orderByChild("UserID").equalTo(FirebaseAuth.getInstance().uid)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                //whenever an actual value has been read
                snapshot.children.forEach {
                    // cast to local db
                    val favoItem = it.getValue(FavoriteItem::class.java)
                    if (favoItem != null) {
                        // whenever the item isnt null AND is of the current user, add it to their view
                        // based on the query
                        adapter.add(FavoItem(favoItem))
                    }
                }
                // maak het visueel zichtbaar door deze adapter ook te binden
                // met de recyclerview
                binding.fragment =
               // binding.recyclerViewFavoItems.adapter = adapter
            }


        })
    }

    */

    private fun DeleteFavoItem(id: String) {
        val query = ParseQuery.getQuery<ParseObject>("FavoriteItems")

        // Retrieve the object by id
        query.whereEqualTo("objectId", id)
        var results = query.find();
        results.forEach() {
            it.deleteInBackground()

           // reload function to check update
        }
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

class FavoItem(val favoriteItem: FavoriteItem) : Item<GroupieViewHolder>() {
    // get the layout you want to have each item to be filled in
    // the 'temp' file
    override fun getLayout(): Int {
        return R.layout.row_add_favo_item
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.tvFavoItem).text = favoriteItem.Name
        viewHolder.itemView.findViewById<ImageView>(R.id.imgCross).setOnClickListener {
            // delete the required items
        }
    }
}
