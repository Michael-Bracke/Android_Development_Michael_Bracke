package secret.santa.application.views.favo;

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.secret.santa.R
import com.secret.santa.databinding.ActivityAccountFavoDetailBinding
import com.secret.santa.databinding.ActivityGroupDetailBinding
import com.secret.santa.views.AccountFavoListSSA
import com.secret.santa.views.GroupDetailSSA
import com.secret.santa.views.GroupOverviewSSA
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
        binding.txtItemNameValue.text = favoName
        binding.btnFavoEdit.setOnClickListener {
            val intent = Intent(binding.root.context, FavoriteEdit::class.java)
            // send extra paramstest²
            intent.putExtra(FavoriteFragment.FAVO_ID,favoId)
            startActivity(intent)
        }


    }
}