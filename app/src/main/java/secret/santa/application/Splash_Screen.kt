package secret.santa.application

import android.content.Intent
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.annotation.RequiresApi
import com.facebook.login.Login
import com.secret.santa.R
import com.secret.santa.views.LoginSSA
import kotlinx.android.synthetic.main.activity_splash__screen.*

class Splash_Screen : AppCompatActivity() {

    private val SPLASH_TIME: Long = 3000

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash__screen)
        // create the animation
        // the id (splash_image) refers to the imageView on the layout file
        // that imageView refers to the drawable animatorVectorDrawable
        // which uses the param. from the original vector file + defines anim properties to it
        // making it an AnimatorVectorDrawable, which is REQUIRED!
        //val animatedVectorDrawable = splash_image.drawable as AnimatedVectorDrawable
        val animatedVectorDrawableLoading = splash_image_loading.drawable as AnimatedVectorDrawable
        // start the animation
        animatedVectorDrawableLoading.start()
        //animatedVectorDrawable.start()
        // delay before heading to the login screen
        Handler().postDelayed({
            startActivity(Intent(this, LoginSSA::class.java))
            finish();
        }, SPLASH_TIME)

    }
}