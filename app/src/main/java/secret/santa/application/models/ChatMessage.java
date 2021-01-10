package secret.santa.application.models;


import com.parse.ParseObject;

import java.time.LocalDate;
import java.util.Date;

public class ChatMessage {
    public String Text;
    public String UserID;
    public String GroupID;
    public String Id;
    public long DateSend;

    public ChatMessage(String id, String text, String userID, String groupID, long dateSend) {
        this.Text = text;
        this.Id = id;
        this.UserID = userID;
        this.GroupID = groupID;
        this.DateSend = dateSend;
    }

    public ChatMessage(){}

    public String GetText() {
        return this.Text;
    }

    public long GetDateSend() {
        return this.DateSend;
    }

    public String GetGroupID() {
        return this.GroupID;
    }

    public String GetUserID() {
        return this.UserID;
    }


    public void StoreDatabase() {
        String messageText = this.GetText();
        long messageDateSend = this.GetDateSend();
        String messageUserID = this.GetUserID();
        String messageGroupID = this.GetGroupID();
        ParseObject ObjectToStore = new ParseObject("Message");
        ObjectToStore.put("Text", messageText);
        ObjectToStore.put("DateSend", messageDateSend);
        ObjectToStore.put("UserID", messageUserID);
        ObjectToStore.put("GroupID", messageGroupID);

        ObjectToStore.saveInBackground();
    }

}