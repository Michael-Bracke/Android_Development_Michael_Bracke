package secret.santa.application.models;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.parse.ParseObject;

import org.json.JSONArray;

import java.util.UUID;

import kotlinx.android.parcel.Parcelize;

@Parcelize
public class Group implements Parcelable {
    public String Name;
    public Boolean IsActive;
    public String UserGroupId;
    public String AdminID;
    public String InviteId;
    public String Id;

    public Group(String name, Boolean isActive, String userGroupId, String adminID, String inviteId) {
        this.Name = name;
        this.IsActive = isActive;
        this.UserGroupId = userGroupId;
        this.InviteId = inviteId;
        this.AdminID = adminID;
    }

    public Group(){}

    public Group(String name) {
        this.Name = name;
    }

    protected Group(Parcel in) {
        Name = in.readString();
        byte tmpIsActive = in.readByte();
        IsActive = tmpIsActive == 0 ? null : tmpIsActive == 1;
        UserGroupId = in.readString();
        AdminID = in.readString();
        Id = in.readString();
    }

    public static final Creator<Group> CREATOR = new Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };

    public String GetName() {
        return this.Name;
    }

    public boolean IsActive() {
        return this.IsActive;
    }

    public String GetAdminID() {
        return this.AdminID;
    }

    public String  GetUserIDs() {
        return this.UserGroupId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Name);
        dest.writeByte((byte) (IsActive == null ? 0 : IsActive ? 1 : 2));
        dest.writeString(UserGroupId);
        dest.writeString(AdminID);
        dest.writeString(Id);
    }
}




