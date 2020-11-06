package com.example.bloodbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodbank.model.Donor;
import com.example.bloodbank.model.Requests;
import com.example.bloodbank.model.UserDetails;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DonorDeatilsActivity extends AppCompatActivity {
    private TextView requestName, requestPhonenumber, requestBloodgroup;
    Button requestCall;

    String childId = "";

    private String Bloodgroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_donor_deatils );


        requestName = findViewById( R.id.txtdname );

        requestPhonenumber = findViewById( R.id.txtdphonenumber );
        requestBloodgroup = findViewById( R.id.txtdbloodgroup );
        requestCall = findViewById( R.id.callnow );

        Bloodgroup = requestBloodgroup.getText().toString();

        childId = getIntent().getStringExtra( "pid" );


        getDonorDeatils( childId );


        requestCall.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String MobileNo = requestPhonenumber.getText().toString();
                Intent callIntent = new Intent( Intent.ACTION_DIAL, Uri.fromParts( "tel", MobileNo, null ) );
                startActivity( callIntent );
            }
        } );


    }


    @Override
    protected void onStart() {
        super.onStart();


    }

    private void getDonorDeatils(String childId) {


        DatabaseReference requests = FirebaseDatabase.getInstance().getReference().child( "Donors" );

        String clearCaracter = childId.replace('.',':').replace(',',';');
        requests.child( clearCaracter ).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    Donor donor = dataSnapshot.getValue( Donor.class );


                    requestName.setText( donor.getDonorname() );


                    requestPhonenumber.setText( donor.getPhonenumber() );
                    requestBloodgroup.setText( donor.getBloodgroup() );

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText( DonorDeatilsActivity.this,"error"+databaseError, Toast.LENGTH_SHORT ).show();

            }


        } );
    }
}