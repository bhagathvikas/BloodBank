package com.example.bloodbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bloodbank.model.Donor;
import com.example.bloodbank.model.Requests;
import com.example.bloodbank.model.UserDetails;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DonorActivity extends AppCompatActivity {
    DatabaseReference donor;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userid;
    TextView requsetCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_donor );
        donor = FirebaseDatabase.getInstance().getReference().child( "Donors" );
        recyclerView = findViewById( R.id.recycler_menu );
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( this );
        linearLayoutManager.setReverseLayout( true );
        linearLayoutManager.setStackFromEnd( true );
        recyclerView.setLayoutManager( linearLayoutManager );
        requsetCall = findViewById( R.id.txtdphonenumber );
        BottomNavigationView bottomNavigationView = findViewById( R.id.bottom_nav );

        bottomNavigationView.setSelectedItemId( R.id.donorlist );

        bottomNavigationView.setOnNavigationItemSelectedListener( new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        startActivity( new Intent( getApplicationContext(), HoomeActivity.class ) );
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


    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<Donor> options =
                new FirebaseRecyclerOptions.Builder<Donor>()
                        .setQuery( donor, Donor.class )
                        .build();
        FirebaseRecyclerAdapter<Donor, DonorViewHolder> adapter=
                new FirebaseRecyclerAdapter<Donor, DonorViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final DonorViewHolder donorViewHolder, int i, @NonNull final Donor donors) {



                        donorViewHolder.tvName.setText( donors.getDonorname() );
                        donorViewHolder.tvBloodgroup.setText( donors.getBloodgroup() );
                        donorViewHolder.tvPhonenumber.setText( donors.getPhonenumber() );


                        donorViewHolder.button.setOnClickListener( new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String MobileNo = donorViewHolder.tvPhonenumber.getText().toString();
                                Intent callIntent = new Intent( Intent.ACTION_DIAL, Uri.fromParts( "tel", MobileNo, null ) );
                                startActivity( callIntent );
                            }
                        } );

                    }

                    @NonNull
                    @Override
                    public DonorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.donor_deatils_layout,parent,false );
                        DonorViewHolder donorViewHolder = new DonorViewHolder( view );
                        return  donorViewHolder;
                    }
                };




                    recyclerView.setAdapter( adapter );
        adapter.startListening();
    }
}



