package com.example.bloodbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodbank.model.Prevalet;
import com.example.bloodbank.model.Requests;
import com.example.bloodbank.model.UserDetails;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserDetailsActivity extends AppCompatActivity {
    private CircleImageView profileImageView;
    private EditText UserName, PhoneNumber, EmailId;

    private TextView profileChangeTextBtn, closeTextBtn, saveTextButton;
    private Button submit;
    CheckBox donateblood;
    private Spinner bloodgroup;
    String sBloodgroup;
    private ProgressDialog progressDialog;



    private Uri imageUri;
    private String myUrl = "", BloodGrp,saveCurrentDate, saveCurrentTime,Donorkey;
    private StorageTask uploadTask;
    private StorageReference storageProfilePrictureRef;
    private String phonenumber, checker = "";
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_user_details );

        SharedPreferences preferences = getSharedPreferences( "UserDetails", MODE_PRIVATE );

        String FirstTime = preferences.getString( "Enter Your Details", "" );

        if (FirstTime.equals( "yes" )) {

            Intent intent = new Intent( UserDetailsActivity.this, HoomeActivity.class );
            startActivity( intent );
            finish();

        } else {


            SharedPreferences.Editor editor = preferences.edit();
            editor.putString( "Enter Your Details", "yes" );
            editor.apply();
        }


        storageProfilePrictureRef = FirebaseStorage.getInstance().getReference().child( "Profile pictures" );

        profileImageView = (CircleImageView) findViewById( R.id.settings_profile_image );
        UserName = findViewById( R.id.etusername );
        PhoneNumber = findViewById( R.id.etphonenumber );
        EmailId = findViewById( R.id.etemail );
        donateblood = findViewById( R.id.chckbox );
        phonenumber = PhoneNumber.getText().toString();

        submit = findViewById( R.id.btnsubmit );
        bloodgroup = (Spinner) findViewById( R.id.spinner );
        progressDialog = new ProgressDialog( this );

        bloodgroup.setAdapter( new ArrayAdapter<String>( this, android.R.layout.simple_spinner_dropdown_item, BloodGroups.bloodGroups ) );


        profileChangeTextBtn = (TextView) findViewById( R.id.profile_image_change_btn );


        userid = user.getUid();

        final String email = EmailId.getText().toString().trim();

        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        EmailId.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (email.matches(emailPattern) && s.length() > 0)
                {
                    Toast.makeText(getApplicationContext(),"valid email address",Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Invalid email address",Toast.LENGTH_SHORT).show();

                }

            }
        } );


        submit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                userInfoSaved();



            }
        } );



    }


        private void userInfoSaved () {
            sBloodgroup = bloodgroup.getSelectedItem().toString();
            String email = EmailId.getText().toString().trim();

            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";



            if (bloodgroup.getSelectedItem().toString().isEmpty()) {
                Toast.makeText( this, "Please select Your Blood Group", Toast.LENGTH_SHORT ).show();
            } else if (TextUtils.isEmpty( UserName.getText().toString() )) {
                Toast.makeText( this, "name is mandatory.", Toast.LENGTH_SHORT ).show();
            } else if (TextUtils.isEmpty( PhoneNumber.getText().toString() ) || PhoneNumber.length() < 10) {
                PhoneNumber.setError( "Valid number is required" );
                PhoneNumber.requestFocus();
                Toast.makeText( this, "phonenumber is mandatory.", Toast.LENGTH_SHORT ).show();

            }


            else {

                uploadData();
            }


        }




        private void uploadData () {

            progressDialog.setTitle( " Cretaing Account" );
            progressDialog.setMessage( "Please Wait..." );
            progressDialog.setCanceledOnTouchOutside( false );
            progressDialog.show();


            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child( "Users" );

            HashMap<String, Object> userMap = new HashMap<>();

            userMap.put( "username", UserName.getText().toString() );
            userMap.put( "Email", EmailId.getText().toString() );
            userMap.put( "phonenumber", PhoneNumber.getText().toString() );
            userMap.put( "bloodgroup", sBloodgroup );
            userMap.put( "pid",userid );


            ref.child( userid ).updateChildren( userMap );

            startActivity( new Intent( UserDetailsActivity.this, HoomeActivity.class ) );
            Toast.makeText( UserDetailsActivity.this, "Account Created", Toast.LENGTH_SHORT ).show();
            finish();
        }

    }













