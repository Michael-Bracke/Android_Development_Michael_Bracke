package secret.santa.application.models;

public class UserGroup {
    public String Name;
    public String Id;
    public String Uid;

    public UserGroup(String name, String id, String uid){
        this.Name = name;
        this.Id = id;
        this.Uid = uid;
    }

    public UserGroup(){}
}
