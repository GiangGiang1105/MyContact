package com.example.contacts.Database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.contacts.Database.Dao.ContactDao;
import com.example.contacts.Model.Contact;

@Database(entities = {Contact.class}, version = 1)
public abstract class ContactRoomDataBase extends RoomDatabase {

    public abstract ContactDao contactDao();
    private static ContactRoomDataBase INSTANCE;
    public static ContactRoomDataBase getDatabase(final Context context){
        if (INSTANCE == null){
            synchronized (ContactRoomDataBase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ContactRoomDataBase.class, "Database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();

                }
            }
        }

        return INSTANCE;
    }


    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){
                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                }
    };

}



