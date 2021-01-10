package secret.santa.application.models;

public class User {
    public String Name;
    public String ProfileImageUrl;
    public String Uid;

    public User(String name, String profileImageUrl, String uid){
        this.Name = name;
        this.ProfileImageUrl = profileImageUrl;
        this.Uid = uid;
    }

    public  User(){}
}
