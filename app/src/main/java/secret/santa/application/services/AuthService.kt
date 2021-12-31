package secret.santa.application.services

import android.text.TextUtils
import android.util.Log
import com.google.firebase.auth.FirebaseAuth

class AuthService {

     fun Register(name:String, email:String, pass:String) : String {
        if (TextUtils.isEmpty(name)) {
            return "Naam is verplicht!"
        } else if (TextUtils.isEmpty(email)) {
            return "Email is verplicht!"
            // TODO adding correct checks for email
        } else if (TextUtils.isEmpty(pass)) {
            return "Wachtwoord is verplicht!"
        }
         // when everything is filled in correctly
         try {
             FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                 email.trim(),
                 pass.trim()
             )
                 .addOnCanceledListener {
                     Log.d("USERCREATION", "CANCELED")
                 }
                 .addOnCompleteListener {
                     Log.d("USERCREATION", "COMPLETED STORAGE")
                     if (!it.isSuccessful) {
                         it.addOnFailureListener {
                             Log.d("USERCREATION", it.message.toString())
                         }

                         return@addOnCompleteListener
                     }
                     Log.e("USERCREATION", "UPLOADING IMAGE TO FIREBASE STORAGE...")
                 }
                 .addOnFailureListener {
                     Log.e("USERCREATION", "" + it.message)
                     return@addOnFailureListener
                 }


         }catch (ex : Exception){
             Log.e("USERCREATION", "" + ex)
             return ex.message!!
         }
         return "UNSUCCESSFULL";
    }
}