package secret.santa.application.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbAdapter {
    myDbHelper helper;
    public DbAdapter(Context context)
    {
        helper = new myDbHelper(context);
    }

    public String getData()
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {myDbHelper.UID,myDbHelper.NAME,myDbHelper.PROFILEIMGURL};
        Cursor cursor =db.query(myDbHelper.TABLE_NAME,columns,null,null,null,null,null);
        StringBuffer buffer= new StringBuffer();
        while (cursor.moveToNext())
        {
            int cid =cursor.getInt(cursor.getColumnIndex(myDbHelper.UID));
            String name =cursor.getString(cursor.getColumnIndex(myDbHelper.NAME));
            String  pimgurl =cursor.getString(cursor.getColumnIndex(myDbHelper.PROFILEIMGURL));
            buffer.append(cid+ "   " + name + "   " + pimgurl +" \n");
        }
        return buffer.toString();
    }

    public long insertData(String name, String ProfileImageUrl, String UID)
    {
        SQLiteDatabase dbb = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.NAME, name);
        contentValues.put(myDbHelper.PROFILEIMGURL, ProfileImageUrl);
        contentValues.put(myDbHelper.UID_FIREBASE, UID);
        long id = dbb.insert(myDbHelper.TABLE_NAME, null , contentValues);
        return id;
    }

    static class myDbHelper extends SQLiteOpenHelper
    {
        private static final String DATABASE_NAME = "myDatabase";    // Database Name
        private static final String TABLE_NAME = "myTable";   // Table Name
        private static final int DATABASE_Version = 1;    // Database Version
        private static final String UID="_id";     // Column I (Primary Key)
        private static final String NAME = "Name";
        private static final String UID_FIREBASE = "UID"; //Column III (Firebase UID)
        private static final String PROFILEIMGURL= "ProfileImageUrl";    // Column III
        private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+
                " ("+UID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+NAME+" VARCHAR(255) ,"
                + PROFILEIMGURL+" VARCHAR(225) ," + UID_FIREBASE+ " VARCHAR(225))";
        private static final String DROP_TABLE ="DROP TABLE IF EXISTS "+TABLE_NAME;
        private Context context;

        public myDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_Version);
            this.context=context;
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                Log.e("Message", "OnUpgrade");
                db.execSQL(DROP_TABLE);
                onCreate(db);
            }catch(Exception ex){
                Log.e("Message", "" + ex);
            }
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            try {
                sqLiteDatabase.execSQL(CREATE_TABLE);
            }catch(Exception ex){
                Log.e("Message", "" + ex);
            }
        }
    }
}
