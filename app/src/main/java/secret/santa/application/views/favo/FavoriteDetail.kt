package secret.santa.application.views.favo;

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.secret.santa.R
import com.secret.santa.databinding.ActivityAccountFavoDetailBinding
import com.secret.santa.databinding.ActivityGroupDetailBinding
import com.secret.santa.views.AccountFavoListSSA
import com.secret.santa.views.GroupDetailSSA
import com.secret.santa.views.GroupOverviewSSA
import secret.santa.application.models.FavoriteItem
import secret.santa.application.models.Group
import secret.santa.application.views.chat.GroupChatLog
import secret.santa.application.views.favo.FavoriteFragment

class FavoriteDetail() : AppCompatActivity() {

    private lateinit var binding: ActivityAccountFavoDetailBinding


    @Override
    // Algemene Oncreate Functie om layout aan te roepen
    override fun onCreate(savedInstanceState: Bundle?) {
        // overerven van param
        super.onCreate(savedInstanceState)
        binding = ActivityAccountFavoDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // get the req param from request (de meegegeven intent)
        val favoName = intent.getStringExtra(FavoriteFragment.FAVO_NAME)
        val favoId = intent.getStringExtra(FavoriteFragment.FAVO_ID)
        // haal correcte favoriteItem op met al zijn eigenschappen
        if(favoId != null)
            GetFavoriteItem(favoId)
        binding.btnFavoEdit.setOnClickListener {
            val intent = Intent(binding.root.context, FavoriteEdit::class.java)
            // send extra paramstest²
            intent.putExtra(FavoriteFragment.FAVO_ID,favoId)
            startActivity(intent)
        }
        binding.ivDeleteItem?.setOnClickListener {
            val ref = FirebaseDatabase.getInstance(getString(R.string.database_instance))
                .getReference("/favoitems/" + favoId);
            ref.removeValue()
            val intent = Intent(binding.root.context, AccountFavoListSSA::class.java)
            startActivity(intent)
        }
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