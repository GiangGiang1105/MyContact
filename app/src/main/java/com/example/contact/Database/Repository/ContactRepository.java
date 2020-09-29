package com.example.contacts.Database.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.contacts.Database.ContactRoomDataBase;
import com.example.contacts.Database.Dao.ContactDao;
import com.example.contacts.Model.Contact;

import java.util.List;

public class ContactRepository {
    private ContactDao contactDao;
    LiveData<List<Contact>> allContact;
    LiveData<List<Contact>> filterContact;
    LiveData<Contact> contact;
    public ContactRepository(Application application){
        ContactRoomDataBase db = ContactRoomDataBase.getDatabase(application);
        contactDao = db.contactDao();
        allContact = contactDao.getAllContact();
    }
    public LiveData<List<Contact>> getAllContact(){
        return allContact;
    }
    public void insert(Contact contact){
        new insertAsyncTask(contactDao).execute(contact);
    }
    public LiveData<Contact> getContact(int id){
        return contact;
    }
    public LiveData<List<Contact>> getFilterContact(String filter){
        filterContact = contactDao.getFilterContact(filter);
        return filterContact;
    }
    public void deleteAll(){
        contactDao.deleteAll();
    }
    public void update(Contact contact){
        contactDao.update(contact);
    }
    public void delete(Contact contact){
        contactDao.delete(contact);
    }
    private static class insertAsyncTask extends AsyncTask<Contact, Void, Void> {
        private ContactDao contactDao;

        public insertAsyncTask(ContactDao contactDao) {
            this.contactDao = contactDao;
        }

        @Override
        protected Void doInBackground( final Contact... contacts) {
            contactDao.insert(contacts[0]);
            return null;
        }
    }
}
