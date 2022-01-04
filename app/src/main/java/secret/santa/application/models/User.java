package secret.santa.application.models;

import com.google.firebase.database.PropertyName;

public class User {
    public String Name;
    public String ProfileImageUrl;
    public String Uid;

    public User(String name, String profileImageUrl, String uid){
        this.Name = name;
        this.ProfileImageUrl = profileImageUrl;
        this.Uid = uid;
    }

    public User(){}

    @PropertyName("Name")
    public String getName() {
        return this.Name;
    }
    @PropertyName("Name")
    public void setName(String name) {
        this.Name = name;
    }
    @PropertyName("ProfileImageUrl")
    public String getProfileImageUrl() {
        return this.ProfileImageUrl;
    }
    @PropertyName("ProfileImageUrl")
    public void setProfileImageUrl(String profileImageUrl) {
        this.ProfileImageUrl = profileImageUrl;
    }
    @PropertyName("Uid")
    public String getUid() {
        return this.Uid;
    }
    @PropertyName("Uid")
    public void setUid(String uid) {
        this.Uid = uid;
    }
}
