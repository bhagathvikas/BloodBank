package com.example.bloodbank;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class RequestViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView tvName,tvAge,tvHospital,tvPhonenumber,tvBloodgroup,tvUsername;
    public ItemClickListner listner;
    public Button button;






    public RequestViewHolder(@NonNull View itemView) {
        super(itemView);




 tvName = (TextView)itemView.findViewById(R.id.txtname);


        tvBloodgroup = (TextView)itemView.findViewById(R.id.txtbloodgroup);










    }
    public void setItemClickListner(ItemClickListner listner) {
        this.listner = listner;

    }

    @Override
    public void onClick(View view)
    {
        listner.onClick(view, getAdapterPosition(), false);
    }
}

