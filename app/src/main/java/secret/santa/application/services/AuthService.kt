package secret.santa.application.services

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import com.secret.santa.R
import com.secret.santa.views.MainSSA
import java.util.*
import android.os.Bundle
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.parse.Parse.getApplicationContext

class AuthService {


     fun Register( name:String, email:String, pass: String, context: Context, callback: (String) -> Unit): String {
        if (name.isNullOrEmpty()) {
            callback(context.getString(R.string.Name_Error))
        } else if (email.isNullOrEmpty()) {
            callback(context.getString(R.string.Email_Error))
            // TODO adding correct checks for email
        } else if (pass.isNullOrEmpty()) {
            callback(context.getString(R.string.Pass_Error))
        }
         // when everything is filled in correctly
         try {
             FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                 email.trim(),
                 pass.trim()
             )
                 .addOnCanceledListener {
                     callback("CANCELED")
                 }
                 .addOnCompleteListener {
                     Log.d("USERCREATION", "COMPLETED STORAGE")
                     if (!it.isSuccessful) {
                         it.addOnFailureListener {
                             Log.d("USERCREATION", it.message.toString())
                         }
                     } else {
                         Log.e("USERCREATION", "UPLOADING IMAGE TO FIREBASE STORAGE...")
                         callback("SUCCESS")
                     }
                 }
                 .addOnFailureListener {
                     Log.e("USERCREATION", "" + it.message)
                     callback(it.message!!)
                 }


         }catch (ex : Exception){
             Log.e("USERCREATION", "" + ex)
             callback(ex.message!!)
         }
         return "UNSUCCESSFULL";
    }
}