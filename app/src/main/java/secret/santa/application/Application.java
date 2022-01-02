package secret.santa.application;

import android.content.Context;
import android.icu.text.UnicodeSetIterator;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.secret.santa.R;

public class Application extends AppCompatActivity {

    public static String myGetString(Context context) {
        return context.getString(R.string.Email_Error);
    }


    private static Context context;

    public static Context GetContext(){
        return context;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
    }
}
