package secret.santa.application.dbParse;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class dbParseService {
    public dbParseService(){}
    public ArrayList<ParseUser> retrieveAllUsers(){
        final ArrayList<ParseUser> listOfAllUsers = new ArrayList<ParseUser>();
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereExists("objectId");
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> users, ParseException e) {
                if (e == null) {
                    // The query was successful, returns the users that matches
                    // the criterias.
                    //Log.d("USER", user.get("name").toString());
                    listOfAllUsers.addAll(users);
                } else {
                    // Something went wrong.
                }
            }

        });
        return listOfAllUsers;
    }
}
