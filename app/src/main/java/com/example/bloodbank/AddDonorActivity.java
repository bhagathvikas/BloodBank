package com.example.bloodbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddDonorActivity extends AppCompatActivity {
    private EditText UserName,PhoneNumber,EmailId;
    private Button submit;
    CheckBox donateblood;
    private Spinner bloodgroup;
    String sBloodgroup,saveCurrentDate, saveCurrentTime,DonorRandomKey;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_add_donor );


        UserName = findViewById( R.id.etusername );
        PhoneNumber = findViewById( R.id.etphonenumber );
        EmailId = findViewById( R.id.etemail );
        donateblood = findViewById( R.id.chckbox );


        submit = findViewById( R.id.btnsubmit );
        bloodgroup = (Spinner) findViewById( R.id.spinner );
        progressDialog = new ProgressDialog( this );

        bloodgroup.setAdapter( new ArrayAdapter<String>( this, android.R.layout.simple_spinner_dropdown_item, BloodGroups.bloodGroups ) );

        BottomNavigationView bottomNavigationView = findViewById( R.id.bottom_nav );

        bottomNavigationView.setSelectedItemId( R.id.Adddonor );

        bottomNavigationView.setOnNavigationItemReselectedListener( new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.donorlist:
                        startActivity( new Intent( getApplicationContext(), DonorDeatilsActivity.class ) );
                        overridePendingTransition( 0, 0 );
                        return ;
                    case R.id.home:
                        startActivity( new Intent( getApplicationContext(), HoomeActivity.class ) );
                        overridePendingTransition( 0, 0 );
                        return ;
                    case R.id.account:
                        startActivity( new Intent( getApplicationContext(), AccountDetailsActivity.class ) );
                        overridePendingTransition( 0, 0 );
                        return ;
                }
                return ;

            }
        } );
        submit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DonorInfo();
            }
        } );


    }

    private void DonorInfo() {
        sBloodgroup = bloodgroup.getSelectedItem().toString();


        if (bloodgroup.getSelectedItem().toString().isEmpty()) {
            Toast.makeText( this, "Please select Your Blood Group", Toast.LENGTH_SHORT ).show();
        } else if (TextUtils.isEmpty( UserName.getText().toString() )) {
            Toast.makeText( this, "name is mandatory.", Toast.LENGTH_SHORT ).show();
        } else if (TextUtils.isEmpty( EmailId.getText().toString() )) {
            Toast.makeText( this, "Email Required", Toast.LENGTH_SHORT ).show();
        } else if (TextUtils.isEmpty( PhoneNumber.getText().toString() ) || PhoneNumber.length() < 10) {
            PhoneNumber.setError( "Valid number is required" );
            PhoneNumber.requestFocus();
            Toast.makeText( this, "phonenumber is mandatory.", Toast.LENGTH_SHORT ).show();

        }


        else {

            uploadData();
        }

    }

    private void uploadData() {
        progressDialog.setTitle( " Cretaing Account" );
        progressDialog.setMessage( "Please Wait..." );
        progressDialog.setCanceledOnTouchOutside( false );
        progressDialog.show();


        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH mm ss ");
        saveCurrentTime = currentTime.format(calendar.getTime());

        DonorRandomKey = saveCurrentDate + saveCurrentTime;




        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child( "Donors" );

        ref.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!(dataSnapshot.child("request").child(DonorRandomKey).exists())) {
                    HashMap<String, Object> userMap = new HashMap<>();

                    userMap.put( "donorname", UserName.getText().toString() );
                    userMap.put( "email", EmailId.getText().toString() );
                    userMap.put( "phonenumber", PhoneNumber.getText().toString() );
                    userMap.put( "bloodgroup", sBloodgroup );
                    userMap.put( "donorkey", DonorRandomKey );
                    userMap.put("date", saveCurrentDate);
                    userMap.put("time", saveCurrentTime);


                    String clearCaracter = DonorRandomKey.replace('.',':').replace(',',';');

                    ref.child( clearCaracter ).updateChildren( userMap )
                            .addOnCompleteListener( new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(AddDonorActivity.this, "Donor Added", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();

                                        Intent intent = new Intent(AddDonorActivity.this, HoomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else
                                    {
                                        progressDialog.dismiss();
                                        Toast.makeText(AddDonorActivity.this, "Network Error" , Toast.LENGTH_SHORT).show();
                                    }

                                }
                            } );

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

    }
}
