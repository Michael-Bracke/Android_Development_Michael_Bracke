package com.secret.santa



import android.content.Context
import android.content.res.Resources
import org.junit.Test
import com.parse.Parse
import org.junit.Assert.*
import secret.santa.application.Application
import secret.santa.application.services.AuthService
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class UnitTest {

    val mAuth : AuthService = AuthService()
    val app : Application = Application()

    @Mock
    private var resources: Resources? = null
    private var contextMock: Context? = null
    @Before
    fun init() {
        resources = Resources.getSystem()
    }

    @Test
    fun test(){
        assertEquals("MY STRING", resources!!.getString(R.string.Email_Error));
    }

    @Test
    fun emptyUsernameReturnsError() {

        val inptName = "";
        val inptEmail = "test25@live.be";
        val inptPass = "whatisthislogic12345";

        mAuth.Register(inptName,inptEmail, inptPass, contextMock!!) {
            returnVal -> assertEquals(returnVal, "Please check your name")
        }
    }

    @Test
    fun emptyEmailReturnsError() {


        val inptName = "qzdqzdqzdqd";
        val inptEmail = "";
        val inptPass = "whatisthislogic12345";

        mAuth.Register(inptName,inptEmail, inptPass, contextMock!!) {
                returnVal -> assertEquals(returnVal, "Please check your email")
        }
    }

    @Test
    fun emptyPassReturnsError() {

        val inptName = "qzdqzddqzdzad";
        val inptEmail = "test25@live.be";
        val inptPass = "";

        mAuth.Register(inptName,inptEmail, inptPass, contextMock!!) {
                returnVal -> assertEquals(returnVal, "Please check your password")
        }
    }
}