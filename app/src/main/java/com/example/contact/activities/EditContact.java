package com.example.contacts.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.contacts.Model.Contact;
import com.example.contacts.R;
import com.example.contacts.ViewModel.ContactListViewModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class EditContact extends AppCompatActivity {
    private ImageButton editImage;
    private EditText editTextName;
    private EditText editTextSurname;
    private EditText editTextPhone;
    private EditText editTextEmail;
    private ImageView checkView;
    private ImageView backView;
    private TextView textView;
    private ContactListViewModel contactListViewModel;
    private String name;
    private String surname;
    private String phone;
    private String email;
    private String ulr_image;
    private int ID;
    private boolean star;
    private Contact contact;
    private Intent intent;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z.]+";
    private static final int REQUEST_PERMISSIONS = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        getView();
        setUp();
        textView.setText("Edit a contact");
        checkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contact = getContact();
                editContact(contact);
            }
        });
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentBack();
            }
        });
        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               editImageDialog();
            }
        });
    }
    @SuppressLint("LongLogTag")
    private void setUp(){
        intent = getIntent();
        ID = intent.getIntExtra("ID", 0);
        name = intent.getStringExtra("LastName");
        surname = intent.getStringExtra("FirstName");
        phone = intent.getStringExtra("Phone");
        email = intent.getStringExtra("Email");
        ulr_image = intent.getStringExtra("Url_image");
        star = intent.getBooleanExtra("Star", false);
        editTextName.setText(name);
        editTextSurname.setText(surname);
        editTextPhone.setText(phone);
        editTextEmail.setText(email);
        if (!(ulr_image == null)) {
            Bitmap thumbnail = (BitmapFactory.decodeFile(ulr_image));

            Log.w("path of image from gallery......******************.........", ulr_image + "");
            editImage.setImageBitmap(CustomImagePerson.getResizedBitmap(thumbnail, 400));
        }
    }
    private void getView(){
        checkView = (ImageView) findViewById(R.id.check);
        backView = (ImageView) findViewById(R.id.back);
        editTextName = (EditText) findViewById(R.id.name);
        editTextSurname = (EditText) findViewById(R.id.surname);
        editTextPhone = (EditText) findViewById(R.id.phone);
        editTextEmail = (EditText) findViewById(R.id.email);
        textView = (TextView)findViewById(R.id.textView);
        editImage = (ImageButton) findViewById(R.id.imageButton4);
    }
    private void editContact(final Contact contact){
        contactListViewModel = ViewModelProviders.of(this).get(ContactListViewModel.class);
        if (contact.getLastName().trim().equals("") ){
            Toast.makeText(getApplication(), "Edit a contact unsuccessful! Add a name for the name field!", Toast.LENGTH_SHORT).show();
        }
        else if (contact.getPhone().trim().equals("")){
            Toast.makeText(getApplication(), "Edit a contact unsuccessful! Add a phonenumber for the phone field", Toast.LENGTH_SHORT).show();
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
                    contactListViewModel.update(contact);
                }
            });
            IntentBack();
        }

    }
    private Contact getContact(){
        name = String.valueOf(editTextName.getText());
        surname = String.valueOf(editTextSurname.getText());
        phone = String.valueOf(editTextPhone.getText());
        email = String.valueOf(editTextEmail.getText());
        Contact contact = new Contact(ID, surname, name, phone, email, ulr_image,star );
        return contact;
    }
    private void IntentBack(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    private void editImage(final Contact contact) {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(EditContact.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(EditContact.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE))) {

            } else {
                ActivityCompat.requestPermissions(EditContact.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);
            }
        } else {
            Log.e("Else", "Else");
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
                    editImage.setImageBitmap(bitmap);
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
                ulr_image = c.getString(columnIndex);

                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(ulr_image));

                Log.w("path of image from gallery......******************.........", ulr_image+"");
                editImage.setImageBitmap(CustomImagePerson.getResizedBitmap(thumbnail, 400));
            }
        }
    }
    private void editImageDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Set Title.
        builder.setTitle("Select an Animal");

        // Add a list
        final String[] questions = {"Delete image", "Choose a new image"};
        builder.setItems(questions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String question = questions[which];

                dialog.dismiss();
                if (question.equals("Choose a new image")){
                    editImage(contact);
                }
                else {
                    ulr_image = null;
                    editImage.setImageResource(R.drawable.camera);
                }
            }
        });

        //
        builder.setCancelable(true);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


}