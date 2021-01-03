package com.example.secret_santa_app.dbParse;


import android.app.Application;
import com.parse.Parse;

public class App extends Application {
    // Initializes Parse SDK as soon as the application is created
    @Override
    public void onCreate() {
        super.onCreate();
        // app gegevens vanuit parse db dashboard
        // mss later nog omgezet naar constante in string variabelen
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("DmI1r1lm4UChhY3qr1zvC2zoQOFmV85bnR1Dt1pY")
                .clientKey("irHjebK63yML8yNIRyM2xm9p4CH4OJrPDEt3rYW6")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }

}
