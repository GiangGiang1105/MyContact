package com.example.contacts.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Comparator;

@Entity(tableName = "Contact")
public class Contact{
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo( name = "ID")
    private int ID;
    @ColumnInfo( name = "FirstName")
    private String FirstName;
    @NonNull
    @ColumnInfo( name = "LastName")
    private String LastName;
    @NonNull
    @ColumnInfo( name = "Phone")
    private String Phone;
    @ColumnInfo( name = "Email")
    private String Email;
    @ColumnInfo(name = "star")
    private boolean Star;
    @ColumnInfo(name = "ULR")
    private String url_image;
    public Contact(){

    }
    public Contact( String firstName, @NonNull String lastName, @NonNull String phone, String email, String url_image, boolean star) {
        this.ID = ID;
        FirstName = firstName;
        LastName = lastName;
        Phone = phone;
        Email = email;
        this.url_image = url_image;
        this.Star = star;
    }
    public Contact( int ID, String firstName, @NonNull String lastName, @NonNull String phone, String email) {
        this.ID = ID;
        FirstName = firstName;
        LastName = lastName;
        Phone = phone;
        Email = email;
    }
    public Contact( int ID, String firstName, @NonNull String lastName, @NonNull String phone, String email, String url_image, boolean star) {
        this.ID = ID;
        FirstName = firstName;
        LastName = lastName;
        Phone = phone;
        Email = email;
        this.url_image = url_image;
        this.Star = star;

    }

    public boolean isStar() {
        return Star;
    }

    public void setStar(boolean star) {
        Star = star;
    }

    public String getUrl_image() {
        return url_image;
    }

    public void setUrl_image(String url_image) {
        this.url_image = url_image;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    @NonNull
    public String getLastName() {
        return LastName;
    }

    public void setLastName(@NonNull String lastName) {
        LastName = lastName;
    }

    @NonNull
    public String getPhone() {
        return Phone;
    }

    public void setPhone(@NonNull String phone) {
        Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public static Comparator<Contact> ContactComparator = new Comparator<Contact>() {

        public int compare(Contact s1, Contact s2) {
            String contact1 = s1.getLastName().toUpperCase();
            String contact2 = s2.getLastName().toUpperCase();

            //ascending order
            return contact1.compareTo(contact2);

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }};
}
