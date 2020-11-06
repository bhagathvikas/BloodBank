package com.example.bloodbank;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodbank.model.UserDetails;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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

public class AccountDetailsActivity extends AppCompatActivity {
    private CircleImageView profileImageView;
    private EditText UserName, PhoneNumber, EmailId,bloodgroup;
    String emailid, saveCurrentDate, saveCurrentTime,DonorKey;




    private TextView profileChangeTextBtn, closeTextBtn, saveTextButton;
    private Button submit;


    private Uri imageUri;
    private String myUrl = "", BloodGrp;
    private StorageTask uploadTask;
    private StorageReference storageProfilePrictureRef;
    private String checker = "";
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_account_details );




        storageProfilePrictureRef = FirebaseStorage.getInstance().getReference().child( "Profile pictures" );



        profileImageView = (CircleImageView) findViewById( R.id.settings_profile_image );
        UserName = findViewById( R.id.etusername );

        PhoneNumber = findViewById( R.id.etphonenumber );
        EmailId = findViewById( R.id.etemail );
        bloodgroup = findViewById( R.id.etbloodgroup );
        submit = findViewById( R.id.btnsubmit );
        emailid = EmailId.getText().toString();
        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        BottomNavigationView bottomNavigationView = findViewById( R.id.bottom_nav );

        bottomNavigationView.setSelectedItemId( R.id.account );

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
                        return true ;
                }

                return false;
            }
        } );



        profileChangeTextBtn = (TextView) findViewById( R.id.profile_image_change_btn );


        userid = user.getUid();
        EmailId.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (emailid.isEmpty()){
                    Toast.makeText( AccountDetailsActivity.this, "Enter Email",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (emailid.matches(emailPattern) && s.length() > 0)
                {
                    Toast.makeText(AccountDetailsActivity.this,"valid email address",Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Toast.makeText(AccountDetailsActivity.this,"Invalid email address",Toast.LENGTH_SHORT).show();

                }

            }
        } );


        userInfoDisplay( profileImageView, UserName, EmailId, PhoneNumber, bloodgroup );





        submit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    updateOnlyUserInfo();
                       uploadImage();



            }
        } );


        profileChangeTextBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checker = "clicked";


                CropImage.activity( imageUri )
                        .setAspectRatio( 1, 1 )
                        .start( AccountDetailsActivity.this );
            }
        } );
    }








    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        Intent intent = new Intent(  );

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult( data );
            imageUri = result.getUri();

            profileImageView.setImageURI( imageUri );
        } else {

            intent.putExtra( "imageUri", Uri.parse( "android.resource://com.example.bloodbank/drawable/roundaccountbuttonwithuserinside" ) );



            startActivity( new Intent( AccountDetailsActivity.this, AccountDetailsActivity.class ) );
            finish();
        }
    }






    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog( this );
        progressDialog.setTitle( "Updating Profile" );
        progressDialog.setMessage( "Please wait, while we are updating your account information" );
        progressDialog.setCanceledOnTouchOutside( false );
        progressDialog.show();

        if (imageUri != null) {
            final StorageReference fileRef = storageProfilePrictureRef
                    .child( userid + ".jpg" );

            uploadTask = fileRef.putFile( imageUri );

            uploadTask.continueWithTask( new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return fileRef.getDownloadUrl();
                }
            } )
                    .addOnCompleteListener( new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUrl = task.getResult();
                                myUrl = downloadUrl.toString();




                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child( "Users" );


                                HashMap<String, Object> userMap = new HashMap<>();
                                userMap.put( "username", UserName.getText().toString() );
                                userMap.put( "Email", emailid);
                                userMap.put( "phonenumber", PhoneNumber.getText().toString() );
                                userMap.put( "bloodgroup", bloodgroup.getText().toString() );
                                userMap.put( "image", myUrl );
                                userMap.put( "pid", DonorKey );
                                ref.child( userid ).updateChildren( userMap );

                                progressDialog.dismiss();

                                startActivity( new Intent( AccountDetailsActivity.this, AccountDetailsActivity.class ) );
                                Toast.makeText( AccountDetailsActivity.this, "Profile Info update successfully.", Toast.LENGTH_SHORT ).show();
                                finish();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText( AccountDetailsActivity.this, "Error.", Toast.LENGTH_SHORT ).show();
                            }
                        }
                    } );
        } else {

            Toast.makeText( this, "image is not selected.", Toast.LENGTH_SHORT ).show();

        }

    }
    private void updateOnlyUserInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child( "Users" );



        HashMap<String, Object> userMap = new HashMap<>();

        userMap.put( "username", UserName.getText().toString() );
        userMap.put( "Email", emailid );
        userMap.put( "phonenumber", PhoneNumber.getText().toString() );
        userMap.put( "bloodgroup", bloodgroup.getText().toString() );
        userMap.put( "pid", DonorKey );

        ref.child( userid ).updateChildren( userMap );

        startActivity( new Intent( AccountDetailsActivity.this, AccountDetailsActivity.class ) );
        Toast.makeText( AccountDetailsActivity.this, "Profile Details update successfully.", Toast.LENGTH_SHORT ).show();
        finish();
    }
    private void userInfoDisplay(final CircleImageView profileImageView, final EditText userName, final EditText emailId, final EditText phoneNumber, final EditText bloodgroup) {
        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child( "Users" ).child( userid );



        UsersRef.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    UserDetails userDetails = dataSnapshot.getValue(UserDetails.class);



                        Picasso.get().load( userDetails.getImage() ).into( profileImageView );
                        userName.setText( userDetails.getUsername() );
                        emailId.setText( userDetails.getEmail() );
                        phoneNumber.setText( userDetails.getPhonenumber() );
                        bloodgroup.setText( userDetails.getBloodgroup() );




                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Intent intent = new Intent( AccountDetailsActivity.this,HoomeActivity.class );
                startActivity( intent );
                finish();

            }
        } );
    }


}


