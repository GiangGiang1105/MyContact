package com.example.contact.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;

import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contacts.Model.Contact;
import com.example.contacts.R;
import com.example.contacts.ViewModel.ContactListViewModel;
import com.example.contacts.activities.EditContact;
import com.example.contacts.activities.MainActivity;

public class ContactDetail extends AppCompatActivity {
    private ImageView imageView;
    private TextView nameText;
    private TextView phoneText;
    private ImageView backView;
    private ImageView phoneView;
    private ImageView smsView;
    private ImageView shareView;
    private ImageView videoCall;
    private ImageView buttonStar;
    private Intent intent;
    private Contact contact;
    private ContactListViewModel contactListViewModel;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_contact_detail);
        contactListViewModel =  ViewModelProviders.of(this).get(ContactListViewModel.class);
        getView();
        setUp();
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setIntentBack();
            }
        });
        phoneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentCall();
            }
        });
        shareView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentShare();
            }
        });
        smsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentMess();
            }
        });
        videoCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoCall();
            }
        });
        final boolean star = contact.isStar();
        buttonStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contact.setStar(!contact.isStar());
                UpdateStar(contact);
                if (contact.isStar()){
                    buttonStar.setImageDrawable(getDrawable(R.drawable.star));
                }
                else{
                    buttonStar.setImageDrawable(getDrawable(R.drawable.star_outline));
                }
            }
        });
    }
    private void IntentShare(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("smsto:"));
        intent.setType("vnd.android-dir/mms-sms");
        intent.putExtra("address", contact.getPhone());
        startActivity(intent);
    }
    private void IntentMess(){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + contact.getPhone()));
        startActivity(intent);
    }
    private void IntentCall(){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", contact.getPhone(), null));
        startActivity(intent);

    }
    private void VideoCall(){
        Toast.makeText(getApplication(), "Now, We can't implement this function. Thank! ", Toast.LENGTH_SHORT).show();
    }
    private void UpdateStar(final Contact Up_contact){
        AsyncTask.execute(new Runnable() {

            @Override
            public void run() {
                contactListViewModel.update(Up_contact);
            }
        });
    }
    private void getView(){
        imageView = (ImageView) findViewById(R.id.imageView);
        nameText = (TextView) findViewById(R.id.nameText);
        phoneText = (TextView) findViewById(R.id.phoneView);
        backView = (ImageView)findViewById(R.id.back);
        phoneView = (ImageView)findViewById(R.id.callImage);
        smsView = (ImageView)findViewById(R.id.MessageImage);
        shareView = (ImageView)findViewById(R.id.share);
        videoCall = (ImageView) findViewById(R.id.VideoImage);
        buttonStar = (ImageView) findViewById(R.id.button_star);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("LongLogTag")
    private void setUp(){
        intent = getIntent();
        int id = intent.getIntExtra("ID", 0);
        String url = intent.getStringExtra("Url_image");
        boolean star = intent.getBooleanExtra("Star", false);
        String firstName = intent.getStringExtra("FirstName");
        String lastName = intent.getStringExtra("LastName");
        String phone = intent.getStringExtra("Phone");
        String email = intent.getStringExtra("Email");
        contact = new Contact(id, firstName, lastName, phone, email, url, star);
        nameText.setText(contact.getLastName());
        if (contact.getPhone().length() < 4){
            phoneText.setText(contact.getPhone());
        }
        else if (contact.getPhone().length() < 8){
            phoneText.setText(contact.getPhone().substring(0, 3) + " " + contact.getPhone().substring(3,contact.getPhone().length() ) );
        }
        else{
            phoneText.setText(contact.getPhone().substring(0, 3) + " " + contact.getPhone().substring(3, 7) + " "+contact.getPhone().substring(7,contact.getPhone().length() ) );
        }

        if (!(contact.getUrl_image() == null)) {
            Bitmap thumbnail = (BitmapFactory.decodeFile(contact.getUrl_image()));

            Log.i("path of image from gallery......******************.........", contact.getUrl_image() + "");
            imageView.setImageBitmap(com.example.contacts.activities.CustomImagePerson.getResizedBitmap(thumbnail, 400));
        }
        if (contact.isStar()){
            buttonStar.setImageDrawable(getDrawable(R.drawable.star));
        }

    }
    private void setIntentBack(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_actionbar, menu);

        // If you want Icon display in Overflow Menu.
        // https://stackoverflow.com/questions/19750635/icon-in-menu-not-showing-in-android
        if (menu instanceof MenuBuilder) {
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.remove){
            AlertDelete();
        }else if (item.getItemId() == R.id.edit){
            setIntentFixContact();
        }
        return super.onOptionsItemSelected(item);
    }
    private void setIntentFixContact(){
        Intent intent = new Intent(this, EditContact.class);
        intent.putExtra("boolean", false);
        intent.putExtra("ID", contact.getID());
        intent.putExtra("FirstName", contact.getFirstName());
        intent.putExtra("LastName",contact.getLastName());
        intent.putExtra("Phone", contact.getPhone());
        intent.putExtra("Email", contact.getEmail());
        intent.putExtra("Url_image", contact.getUrl_image());
        intent.putExtra("Star", contact.isStar());
        startActivity(intent);
    }
    private void AlertDelete(){
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Delete");
        b.setMessage("Do you have delete this contact?");
        b.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        contactListViewModel.delete(contact);
                        setIntentBack();
                    }
                });
            }
        });
        b.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog al = b.create();
        al.show();
    }

}