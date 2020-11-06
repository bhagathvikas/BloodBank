package com.example.bloodbank.Notifications;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.example.bloodbank.HoomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseIdService extends FirebaseMessagingService
{
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken( s );


        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        String refreshToken = FirebaseInstanceId.getInstance().getToken(  );

        if (firebaseUser!=null){
            updateToken(refreshToken);
        }
    }

    private void updateToken(String refreshToken) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Token");

        Token token = new Token( refreshToken );
        reference.child( firebaseUser.getUid() ).setValue( token );
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived( remoteMessage );
        String sented = remoteMessage.getData().get( "sented" );

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        if (firebaseUser != null && sented.equals( firebaseUser.getUid() )){
            sendNotification(remoteMessage);
        }
    }

    private void sendNotification(RemoteMessage remoteMessage) {

        String users = remoteMessage.getData().get( "user" );
        String icon = remoteMessage.getData().get( "icon" );
        String title = remoteMessage.getData().get( "title" );
        String body = remoteMessage.getData().get( "body" );

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int i = Integer.parseInt( users.replaceAll( "[\\D]", "" ) );

        Intent intent = new Intent( this, HoomeActivity.class );
        Bundle bundle = new Bundle(  );
        bundle.putString( "userid", users );


        startActivity( intent );
    }
}
