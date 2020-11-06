package com.example.bloodbank;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DonorViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView tvName,tvAge,tvHospital,tvPhonenumber,tvBloodgroup,tvUsername;
    public ItemClickListner listner;
    public Button button;






    public DonorViewHolder(@NonNull View itemView) {
        super(itemView);




        tvName = (TextView)itemView.findViewById(R.id.txtname);
        tvPhonenumber = itemView.findViewById( R.id.txtdphonenumber );
        button = itemView.findViewById( R.id.callnow );


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



