package com.example.contacts.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.contacts.Database.Repository.ContactRepository;
import com.example.contacts.Model.Contact;

import java.util.List;

public class ContactListViewModel extends AndroidViewModel{
    private LiveData<List<Contact>> allContact;
    private LiveData<List<Contact>> filterContact;
    private LiveData<Contact> contact;
    private LiveData<Boolean> addBoolean;

    private ContactRepository contactRepository;
    public ContactListViewModel(Application application) {
        super(application);
        contactRepository = new ContactRepository(application);
        allContact = contactRepository.getAllContact();
    }
    public LiveData<List<Contact>> getAllContact(){
        return allContact;
    }
    public void insert(Contact contact){
        contactRepository.insert(contact);
    }
    public void update(Contact contact){
        contactRepository.update(contact);
    }
    public void delete(Contact contact){
        contactRepository.delete(contact);
    }
}
