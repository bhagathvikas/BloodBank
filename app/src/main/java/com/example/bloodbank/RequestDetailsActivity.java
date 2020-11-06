package com.example.bloodbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodbank.model.Prevalet;
import com.example.bloodbank.model.Requests;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class RequestDetailsActivity extends AppCompatActivity {

    private TextView requestName,requestAge,requestHospital,requestPhonenumber, requestBloodgroup, requestDate;
     Button requestCall,requestLocation;

   String requestID = "" ,RequestName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_request_details );

        requestName = findViewById( R.id.txtdname );
        requestAge = findViewById( R.id.txtdage );
        requestHospital = findViewById( R.id.txtdhospital );
        requestPhonenumber = findViewById( R.id.txtdphonenumber );
        requestBloodgroup = findViewById( R.id.txtdbloodgroup );
        requestDate = findViewById( R.id.requestdate );
        requestCall = findViewById( R.id.callnow );
        requestLocation = findViewById( R.id.locationbtn );


        requestID = getIntent().getStringExtra("pid");
        RequestName = getIntent().getStringExtra( "name" );

        getRequestDetails(requestID);

        requestLocation.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = "http://maps.google.co.in/maps?q="+requestHospital.getText().toString();
                Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( location ) );
                startActivity( intent );

            }
        } );

        requestCall.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String MobileNo = requestPhonenumber.getText().toString();
                Intent callIntent = new Intent( Intent.ACTION_DIAL, Uri.fromParts("tel", MobileNo, null) );
                startActivity( callIntent );
                            }
        } );


    }







    @Override
    protected void onStart()
    {
        super.onStart();


    }
    private void getRequestDetails(String requestID) {

        DatabaseReference requests = FirebaseDatabase.getInstance().getReference().child("request");

        String clearCaracter = requestID.replace('.',':').replace(',',';');
        requests.child(clearCaracter).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists())
                {
                    Requests request = dataSnapshot.getValue(Requests.class);




                        requestName.setText( request.getName() );
                        requestDate.setText( request.getDate() );

                        requestAge.setText( request.getAge() );
                        requestHospital.setText( request.getHospital() );
                        requestPhonenumber.setText( request.getPhoneNumber() );
                        requestBloodgroup.setText( request.getBloodGroup() );

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText( RequestDetailsActivity.this, "databaseerror"+ databaseError , Toast.LENGTH_SHORT).show();

            }
        });
    }
}