package secret.santa.application.models;



public class FavoriteItem {
    public String Name;
    public String UserID;
    public String ExtraText;
    public Boolean IsActive;
    public String Link;
    public String id;


    public FavoriteItem(String name, Boolean isActive, String userID, String extraText, String link) {
        this.Name = name;
        this.IsActive = isActive;
        this.UserID = userID;
        this.ExtraText = extraText;
        this.Link = link;
    }
    // empty contstr for firebase
    public FavoriteItem(){}

    public String GetName() {
        return this.Name;
    }

    public boolean IsActive() {
        return this.IsActive;
    }

    public String GetUserID() { return this.UserID;  }

    public String GetLink(){
        return this.Link;
    }

    public String GetText(){
        return this.ExtraText;
    }



}

