package com.secret.santa



import android.content.res.Resources
import org.junit.Test
import com.parse.Parse
import org.junit.Assert.*
import secret.santa.application.services.AuthService


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UnitTest {

    val mAuth : AuthService = AuthService()
    @Test
    fun emptyUsernameReturnsError() {

        val inptName = "";
        val inptEmail = "test25@live.be";
        val inptPass = "whatisthislogic12345";

        mAuth.Register(inptName,inptEmail, inptPass) {
            returnVal -> assertEquals(returnVal, "Please check your name")
        }
    }

    @Test
    fun emptyEmailReturnsError() {


        val inptName = "qzdqzdqzdqd";
        val inptEmail = "";
        val inptPass = "whatisthislogic12345";

        mAuth.Register(inptName,inptEmail, inptPass) {
                returnVal -> assertEquals(returnVal, "Please check your email")
        }
    }

    @Test
    fun emptyPassReturnsError() {

        val inptName = "qzdqzddqzdzad";
        val inptEmail = "test25@live.be";
        val inptPass = "";

        mAuth.Register(inptName,inptEmail, inptPass) {
                returnVal -> assertEquals(returnVal, "Please check your password")
        }
    }
}