package com.example.bloodbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
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

public class NeedBloodActivity extends AppCompatActivity {
    private TextView NbName,NbPhonenumber,NbHospital, NbAge;
    private String sName,sPhonenumber, sHosptial,sAge,  sBloodGroup , saveCurrentDate, saveCurrentTime,RequestRandomKey;

    private EditText EtName,EtPhonenumber,EtHospital,EtAge;
    private RadioGroup Gender;

    private Spinner bloodgroup;
    private Button submit;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_need_blood );


        progressDialog = new ProgressDialog( this );

        NbName = (TextView) findViewById( R.id.tvname );
        NbPhonenumber = (TextView) findViewById( R.id.tvphonenumber );
        NbHospital = (TextView) findViewById( R.id.tvhospital );
        NbAge = (TextView) findViewById( R.id.tvage );


        EtHospital = (EditText) findViewById( R.id.ethospital );
        EtName = (EditText) findViewById( R.id.etname );
        EtPhonenumber = (EditText) findViewById( R.id.etphone );
        EtAge = (EditText) findViewById( R.id.etage );


        bloodgroup = (Spinner) findViewById( R.id.spinner );
        BottomNavigationView bottomNavigationView = findViewById( R.id.bottom_nav );

        bottomNavigationView.setSelectedItemId( R.id.home );
        bottomNavigationView.setOnNavigationItemSelectedListener( new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.donorlist:
                        startActivity( new Intent( getApplicationContext(), DonorDeatilsActivity.class ) );
                        overridePendingTransition( 0, 0 );
                        return true;
                    case R.id.Adddonor:
                        startActivity( new Intent( getApplicationContext(), NeedBloodActivity.class ) );
                        overridePendingTransition( 0, 0 );
                        return true;
                    case R.id.account:
                        startActivity( new Intent( getApplicationContext(), AccountDetailsActivity.class ) );
                        overridePendingTransition( 0, 0 );
                        return true;
                }
                return false;
            }
        } );


        bloodgroup.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, BloodGroups.bloodGroups));

        submit = (Button)findViewById(R.id.btsubmit);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckDeatils();
            }
        });
    }

    private void CheckDeatils() {

        sName = EtName.getText().toString();
        sPhonenumber = EtPhonenumber.getText().toString();
        sHosptial = EtHospital.getText().toString();
        sAge = EtAge.getText().toString();


        sBloodGroup = bloodgroup.getSelectedItem().toString();


        if (bloodgroup.getSelectedItem().toString().isEmpty()){
            Toast.makeText(this, "Please select Your Blood Group", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(sName)){
            Toast.makeText(this, "Enter Your name", Toast.LENGTH_SHORT).show();

        }
        else if (TextUtils.isEmpty(sAge)){
            Toast.makeText(this, "Enter Your age", Toast.LENGTH_SHORT).show();

        }
        else if (TextUtils.isEmpty(sPhonenumber)){
            Toast.makeText(this, "Enter Your PhoneNumber", Toast.LENGTH_SHORT).show();

        }
        else if (TextUtils.isEmpty(sHosptial)){
            Toast.makeText(this, "Enter Location", Toast.LENGTH_SHORT).show();

        }
       else {
           SaveTheInfo();
        }














    }

    private void SaveTheInfo() {
        progressDialog.setTitle("Uploading Request");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        
        SaveRequestDetails();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH mm ss ");
        saveCurrentTime = currentTime.format(calendar.getTime());

        RequestRandomKey = saveCurrentDate + saveCurrentTime;



    }

    private void SaveRequestDetails() {
        final DatabaseReference requestref;
        requestref = FirebaseDatabase.getInstance().getReference().child("request");


        requestref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("request").child(RequestRandomKey).exists())){

                    HashMap<String, Object> requestMap = new HashMap<>();

                    requestMap.put("pid", RequestRandomKey);
                    requestMap.put("date", saveCurrentDate);
                    requestMap.put("time", saveCurrentTime);


                    requestMap.put("name", sName);
                    requestMap.put("age", sAge);
                    requestMap.put("PhoneNumber", sPhonenumber);
                    requestMap.put("Hospital", sHosptial);



                    requestMap.put("BloodGroup", sBloodGroup);

                    String clearCaracter = RequestRandomKey.replace('.',':').replace(',',';');

                    requestref.child(clearCaracter).updateChildren(requestMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        Toast.makeText(NeedBloodActivity.this, "Request Uploaded", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();

                                        Intent intent = new Intent(NeedBloodActivity.this, HoomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else
                                    {
                                        progressDialog.dismiss();
                                        Toast.makeText(NeedBloodActivity.this, "Network Error" , Toast.LENGTH_SHORT).show();
                                    }



                                }
                            });
                }
                else
                {
                    Toast.makeText(NeedBloodActivity.this, "this" + sPhonenumber + "Already Exists", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Toast.makeText(NeedBloodActivity.this, "Plz try using another phonenumber", Toast.LENGTH_SHORT).show();


                    Intent intent = new Intent(NeedBloodActivity.this, HoomeActivity.class);
                    startActivity(intent);
                    finish();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}
