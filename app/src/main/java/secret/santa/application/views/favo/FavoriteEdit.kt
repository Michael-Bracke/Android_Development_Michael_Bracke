package secret.santa.application.views.favo;

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.secret.santa.R
import com.secret.santa.databinding.ActivityAccountFavoDetailBinding
import com.secret.santa.databinding.ActivityAccountFavoEditBinding
import com.secret.santa.databinding.ActivityGroupDetailBinding
import com.secret.santa.views.AccountFavoListSSA
import com.secret.santa.views.FavoItem
import com.secret.santa.views.GroupDetailSSA
import com.secret.santa.views.GroupOverviewSSA
import secret.santa.application.models.FavoriteItem
import secret.santa.application.models.Group
import secret.santa.application.views.chat.GroupChatLog
import secret.santa.application.views.favo.FavoriteFragment

class FavoriteEdit() : AppCompatActivity() {

    private lateinit var binding: ActivityAccountFavoEditBinding


    @Override
    // Algemene Oncreate Functie om layout aan te roepen
    override fun onCreate(savedInstanceState: Bundle?) {

        // overerven van param
        super.onCreate(savedInstanceState)
        binding = ActivityAccountFavoEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get the req param from request (de meegegeven intent)
        val favoId = intent.getStringExtra(FavoriteFragment.FAVO_ID)
        Log.e("FAVOITEMEDIT", ""+favoId)
        if(!favoId.isNullOrEmpty()) {
            GetFavoriteItem(favoId)
        }

        binding.btnFavoEdit.setOnClickListener {
            SaveFavoriteItem(favoId)
        }


    }

    private fun SaveFavoriteItem(uid:String?){
        val ref = FirebaseDatabase.getInstance(getString(R.string.database_instance))
            .getReference("/favoitems/" + uid);
        val favItemToUpdate : FavoriteItem = FavoriteItem(
            binding.root.findViewById<EditText>(R.id.txtItemNameValue).editableText.toString(),
            binding.switchIsActive.isChecked,
            FirebaseAuth.getInstance().uid,
            binding.root.findViewById<EditText>(R.id.txtLinkValue).editableText.toString(),
            binding.root.findViewById<EditText>(R.id.txtExtraInfoValue).editableText.toString()
        )
        favItemToUpdate.id = uid
        ref.setValue(favItemToUpdate)
        var int =  Intent(binding.root.context, FavoriteDetail::class.java)
        int.putExtra(FavoriteFragment.FAVO_ID, uid)
        startActivity(int)
    }

    private fun GetFavoriteItem(uid:String) {
        // haal favoItem op basis van uid
        val ref = FirebaseDatabase.getInstance(getString(R.string.database_instance))
            .getReference("/favoitems/" + uid);
        // eventuele filtering kan hieronder
        val query = ref

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                // haal correcte favoriteItem op met al zijn eigenschappen
                val favItem = snapshot.getValue(FavoriteItem::class.java)
                if(favItem != null){
                    (binding.txtItemNameValue as TextView).text = favItem.Name
                    (binding.txtExtraInfoValue as TextView).text = favItem.ExtraText
                    (binding.txtLinkValue as TextView).text = favItem.Link
                    binding.switchIsActive.isChecked = favItem.IsActive
                }
            }


        })
    }


}