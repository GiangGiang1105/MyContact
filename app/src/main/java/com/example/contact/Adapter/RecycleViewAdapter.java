package com.example.contact.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.contacts.Model.Contact;
import com.example.contacts.R;
import com.example.contact.activities.ContactDetail;

import com.example.contacts.Adapter.ContactItemClickListener;

import java.util.ArrayList;
import java.util.List;




public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.RecyclerViewHolder>  {
    private List<Contact> listcontact;
    private List<Contact> listcontactFilter;
    private LayoutInflater layoutInflate;
    private Context context;
    public RecycleViewAdapter(List<Contact> data) {
        this.listcontact = data;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.contact_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {

        holder.nameContact.setText(listcontact.get(position).getLastName() + " " + listcontact.get(position).getFirstName() );
        if (listcontact.get(position).getPhone().length() < 4){
            holder.phoneContact.setText(listcontact.get(position).getPhone());
        }
        else if (listcontact.get(position).getPhone().length() < 8){
            holder.phoneContact.setText(listcontact.get(position).getPhone().substring(0, 3) + " " + listcontact.get(position).getPhone().substring(3, listcontact.get(position).getPhone().length() ) );
        }
        else{
            holder.phoneContact.setText(listcontact.get(position).getPhone().substring(0, 3) + " " + listcontact.get(position).getPhone().substring(3, 7) + " "+listcontact.get(position).getPhone().substring(7,listcontact.get(position).getPhone().length() ) );
        }

        if (!(listcontact.get(position).getUrl_image() == null)) {
            Bitmap thumbnail = (BitmapFactory.decodeFile(listcontact.get(position).getUrl_image()));

            Log.i("path of image from gallery......******************.........", listcontact.get(position).getUrl_image() + "");
            holder.imageView.setImageBitmap(com.example.contacts.activities.CustomImagePerson.getResizedBitmap(thumbnail, 800));
        }
        else{
            holder.imageView.setImageResource(R.drawable.account_circle1);
        }
        if (listcontact.get(position).isStar()){
            holder.imageStar.setBackgroundResource(R.drawable.star1);
        }


        holder.setClickListener(new com.example.contacts.Adapter.ContactItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(view.getContext(), ContactDetail.class);
                intent.putExtra("ID", listcontact.get(position).getID());
                intent.putExtra("FirstName", listcontact.get(position).getFirstName());
                intent.putExtra("LastName",listcontact.get(position).getLastName());
                intent.putExtra("Phone", listcontact.get(position).getPhone());
                intent.putExtra("Email", listcontact.get(position).getEmail());
                intent.putExtra("Url_image", listcontact.get(position).getUrl_image());
                intent.putExtra("Star", listcontact.get(position).isStar());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listcontact == null ? 0 : listcontact.size();
    }
    public Filter getFilter(final List<Contact> contacts){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                List<Contact> filteredList = new ArrayList<>();
                for (Contact row : contacts) {
                    if (row.getLastName().toLowerCase().contains(charString.toLowerCase()) || row.getFirstName().toLowerCase().contains(charString.toLowerCase()) || row.getPhone().contains(charSequence) ) {
                        filteredList.add(row);
                    }
                }
                listcontactFilter = filteredList;
                FilterResults filterResults = new FilterResults();
                filterResults.values = listcontactFilter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listcontact = (ArrayList<Contact>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView nameContact;
        TextView phoneContact;
        TextView textView;
        ImageView imageView;
        ImageView imageStar;
        private ContactItemClickListener contactItemClickListener;
        public void setClickListener(ContactItemClickListener itemClickListener) {
            this.contactItemClickListener= itemClickListener;
        }
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);
            nameContact = (TextView) itemView.findViewById(R.id.name_contact);
            phoneContact = (TextView) itemView.findViewById(R.id.phone_contact);
            imageStar = (ImageView) itemView.findViewById(R.id.imageStar);
            imageView = (ImageView) itemView.findViewById(R.id.imagePeople);
            itemView.setOnClickListener((View.OnClickListener) this);
        }

        @Override
        public void onClick(View view) {
            if (contactItemClickListener != null) contactItemClickListener.onClick(view, getAdapterPosition());
        }
    }

}


