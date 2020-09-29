package com.example.contacts.Database.Dao;

import android.provider.ContactsContract;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.contacts.Model.Contact;

import java.util.List;
import java.util.zip.CheckedOutputStream;

@Dao
public interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(Contact contact);
    @Update
    public void update(Contact contact);
    @Delete
    public void delete(Contact contact);
    @Query("DELETE FROM Contact")
    public void deleteAll();
    @Query("SELECT * FROM Contact ORDER BY LastName ASC ")
    LiveData<List<Contact>> getAllContact();
    @Query("SELECT * FROM Contact WHERE ID = :id" )
    LiveData<Contact> getContact(int id);
    @Query("SELECT * FROM Contact WHERE LastName LIKE ''+:filter +'%'" )
    LiveData<List<Contact>> getFilterContact(String filter);


}
