package com.example.contacts.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.contact.Adapter.RecycleViewAdapter;
import com.example.contact.activities.ContactDetail;
import com.example.contacts.Adapter.ContactItemClickListener;
import com.example.contacts.Model.Contact;
import com.example.contacts.R;
import com.example.contacts.ViewModel.ContactListViewModel;
import com.example.contacts.activities.AddContact; 
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ContactItemClickListener{
    private TextView mAddNewContact;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private List<Contact> listContact;
    private List<Contact> listContactFilter;
    private LiveData<List<Contact>> listLiveData;
    private ContactListViewModel contactListViewModel;
    private RecycleViewAdapter recycleViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contactListViewModel = ViewModelProviders.of(MainActivity.this).get(ContactListViewModel.class);
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        searchView = (SearchView)findViewById(R.id.searchView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactListViewModel.getAllContact().observe(this, new Observer<List<Contact>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChanged(List<Contact> contacts) {
                Collections.sort(contacts, Contact.ContactComparator);
                listContact = favourite(contacts);
                listContactFilter = contacts;
                recycleViewAdapter = new RecycleViewAdapter(listContact);
                recyclerView.setAdapter(recycleViewAdapter);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
               recycleViewAdapter.getFilter(listContactFilter).filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                recycleViewAdapter.getFilter(listContactFilter).filter(s);
                return false;
            }
        });

        mAddNewContact = (TextView)findViewById(R.id.textView2);
        mAddNewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentAddContact();
            }
        });

    }
    private void IntentAddContact(){
        Intent intent = new Intent(MainActivity.this, AddContact.class);
        intent.putExtra("boolean", true);
        startActivity(intent);
    }

    @Override
    public void onClick(View view, int position) {
        final Contact contact = (Contact) listContact.get(position);
        Intent intent = new Intent(this, ContactDetail.class);
        intent.putExtra("Contact", (Serializable) contact);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Exit Application?");
        alertDialogBuilder
                .setMessage("Click yes to exit!")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                moveTaskToBack(true);
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                            }
                        })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        return;
    }
    private List<Contact> favourite(List<Contact> listCT){
        List<Contact> list = new ArrayList<>();
        Iterator<Contact> iter = listCT.iterator();
        while (iter.hasNext()){
            Contact contact = iter.next();
            if (contact.isStar()){
                list.add(contact);
                iter.remove();
            }
        }
        for (Contact contact : listCT){
            list.add(contact);
        }
        return list;
    }

}