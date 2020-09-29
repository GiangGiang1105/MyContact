package com.example.contacts.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.contacts.Model.Contact;
import com.example.contacts.R;
import com.example.contacts.ViewModel.ContactListViewModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class AddContact extends AppCompatActivity {
    private EditText editTextName;
    private EditText editTextSurname;
    private EditText editTextPhone;
    private EditText editTextEmail;
    private ImageView checkView;
    private ImageView backView;
    private ImageButton loadImage;
    private ContactListViewModel contactListViewModel;
    private String name;
    private String surname;
    private String phone;
    private String email;
    private String picturePath ;
    private int ID;
    private Contact contact;
    private Intent intent;
    private static final int REQUEST_PERMISSIONS = 100;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z.]+";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        getView();
        setUp();
        checkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contact = getContact();
                addContact(contact);
            }
        });
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentBack();
            }
        });
        loadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addImage(contact);
            }
        });

    }
    private void setUp(){
        intent = getIntent();
        boolean bool = intent.getBooleanExtra("boolean", false);
        ID = intent.getIntExtra("ID", 0);
        name = intent.getStringExtra("LastName");
        surname = intent.getStringExtra("FirstName");
        phone = intent.getStringExtra("Phone");
        email = intent.getStringExtra("Email");
        editTextName.setText(name);
        editTextSurname.setText(surname);
        editTextPhone.setText(phone);
        editTextEmail.setText(email);
    }
    private void getView(){
        checkView = (ImageView) findViewById(R.id.check);
        backView = (ImageView) findViewById(R.id.back);
        editTextName = (EditText) findViewById(R.id.name);
        editTextSurname = (EditText) findViewById(R.id.surname);
        editTextPhone = (EditText) findViewById(R.id.phone);
        editTextEmail = (EditText) findViewById(R.id.email);
        loadImage = (ImageButton) findViewById(R.id.imageButton4);
    }
    private void addContact(final Contact contact){
        contactListViewModel = ViewModelProviders.of(this).get(ContactListViewModel.class);
        if (contact.getLastName().trim().equals("") ){
            Toast.makeText(getApplication(), "Add a contact unsuccessful! Add a name for the name field!", Toast.LENGTH_SHORT).show();
        }
        else if (contact.getPhone().trim().equals("")){
            Toast.makeText(getApplication(), "Add a contact unsuccessful! Add a phonenumber for the phone field", Toast.LENGTH_SHORT).show();
        }
        else if (!contact.getEmail().equals("") && !contact.getEmail().toString().trim().matches(emailPattern)){
            Toast.makeText(getApplication(), "Invalid email address", Toast.LENGTH_SHORT).show();
        }
        else if(contact.getPhone().length() > 11){
            Toast.makeText(getApplication(), "This is not a phone number. Phone number length is only about 11 numbers.", Toast.LENGTH_SHORT).show();
        }
        else{
            AsyncTask.execute(new Runnable() {

                @Override
                public void run() {
                    contactListViewModel.insert(contact);
                }
            });
            Toast.makeText(getApplication(), "Add a contact successful", Toast.LENGTH_SHORT).show();
            IntentBack();
        }

    }

    private Contact getContact(){
        name = String.valueOf(editTextName.getText());
        surname = String.valueOf(editTextSurname.getText());
        phone = String.valueOf(editTextPhone.getText());
        email = String.valueOf(editTextEmail.getText());
        Contact contact = new Contact(surname, name, phone, email, picturePath, false);
        return contact;
    }
    private void IntentBack(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    private void addImage(final Contact contact){
        if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(AddContact.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(AddContact.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE))) {

            } else {
                ActivityCompat.requestPermissions(AddContact.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);
            }
        } else {
            Log.e("Else", "Else");
            Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 2);
        }

    }
    @SuppressLint("LongLogTag")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
                    bitmap=CustomImagePerson.getResizedBitmap(bitmap, 400);
                    loadImage.setImageBitmap(bitmap);
                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                picturePath = c.getString(columnIndex);

                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));

                Log.w("path of image from gallery......******************.........", picturePath+"");
                loadImage.setImageBitmap(CustomImagePerson.getResizedBitmap(thumbnail, 400));
            }
        }
    }

}