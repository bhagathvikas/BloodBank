package com.example.bloodbank;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;

import com.example.bloodbank.model.Prevalet;
import com.example.bloodbank.model.Requests;
import com.example.bloodbank.model.UserDetails;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;

import androidx.fragment.app.Fragment;
import androidx.navigation.ui.AppBarConfiguration;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class HoomeActivity extends AppCompatActivity  {

    private AppBarConfiguration mAppBarConfiguration;
    DatabaseReference requests;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton floatingActionButton;
    private EditText mSearchField;
    private ImageButton mSearchBtn;
    List<Requests> requestsList;
    Adapter myAdapter;


    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userid;


    Button button;

    private String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_hoome );

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            type = getIntent().getExtras().get( "user" ).toString();
        }







        BottomNavigationView bottomNavigationView = findViewById( R.id.bottom_nav );

        bottomNavigationView.setSelectedItemId( R.id.home );





        requests = FirebaseDatabase.getInstance().getReference().child( "request" );
        recyclerView = findViewById( R.id.recycler_menu );
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( this );
        linearLayoutManager.setReverseLayout( true );
        linearLayoutManager.setStackFromEnd( true );
        recyclerView.setLayoutManager( linearLayoutManager );



       userid= user.getUid();


        Toolbar toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

bottomNavigationView.setOnNavigationItemSelectedListener( new BottomNavigationView.OnNavigationItemSelectedListener() {
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.donorlist:
                startActivity( new Intent( getApplicationContext(), DonorActivity.class ) );
                overridePendingTransition( 0, 0 );
                return true;
            case R.id.Adddonor:
                startActivity( new Intent( getApplicationContext(), NeedBloodActivity.class ) );
                overridePendingTransition( 0, 0 );
                return true ;
            case R.id.account:
                startActivity( new Intent( getApplicationContext(), AccountDetailsActivity.class ) );
                overridePendingTransition( 0, 0 );
                return true ;
        }

        return false;
    }
} );


    }
    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<Requests> options =
                new FirebaseRecyclerOptions.Builder<Requests>()
                        .setQuery( requests, Requests.class )
                        .build();

        FirebaseRecyclerAdapter<Requests, RequestViewHolder> adapter =
                new FirebaseRecyclerAdapter<Requests, RequestViewHolder>( options ) {
                    @Override
                    protected void onBindViewHolder(@NonNull RequestViewHolder requestViewHolder, int i, @NonNull final Requests requests) {


                        requestViewHolder.tvName.setText( requests.getName() );


                        requestViewHolder.tvBloodgroup.setText( requests.getBloodGroup() );


                        requestViewHolder.itemView.setOnClickListener( new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent( HoomeActivity.this, RequestDetailsActivity.class );
                                HoomeActivity.this.overridePendingTransition( R.anim.nav_default_pop_enter_anim, R.anim.nav_default_exit_anim );
                                intent.putExtra( "pid", requests.getPid() );
                                intent.putExtra( "name", requests.getName() );

                                startActivity( intent );

                            }
                        } );


                    }

                    @NonNull
                    @Override
                    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.requests_details_layout, parent, false );
                        RequestViewHolder requestViewHolder = new RequestViewHolder( view );
                        return requestViewHolder;

                    }
                };


        recyclerView.setAdapter( adapter );
        adapter.startListening();
    }

    }



