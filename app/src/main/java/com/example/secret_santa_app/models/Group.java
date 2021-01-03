package com.example.secret_santa_app.models;



import com.parse.ParseObject;
import org.json.JSONArray;

public class Group {
    private String Name;
    private Boolean IsActive;
    private JSONArray UserIDs;

    public Group(String name, Boolean isActive, JSONArray userIDs) {
        this.Name = name;
        this.IsActive = isActive;
        this.UserIDs = userIDs;
    }

    public String GetName() {
        return this.Name;
    }

    public boolean IsActive() {
        return this.IsActive;
    }


    public JSONArray getUserIDs(){
        return this.UserIDs;
    }



    public void StoreDatabase() {
        String groupName = this.GetName();
        Boolean groupActive = this.IsActive();
        JSONArray groupUserIDs = this.getUserIDs();

        ParseObject groupObjectToStore = new ParseObject("Group");
        groupObjectToStore.put("Name", groupName);
        groupObjectToStore.put("Active", groupActive);
        groupObjectToStore.put("UserIDs", groupUserIDs);

        groupObjectToStore.saveInBackground();
    }

}




