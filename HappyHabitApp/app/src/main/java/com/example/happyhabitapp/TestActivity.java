/**
 * This activity is used for testing firebase
 * Author: Frank Li
 */


package com.example.happyhabitapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TestActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener, FirestoreCallback {
    ArrayList<String> test = new ArrayList<String>();
    FireBase fire = new FireBase();
    Calendar date = Calendar.getInstance();
    ArrayList<User> requesters = new ArrayList<User>();

    private final String TAG = "TestActivity";
    ArrayList<Habit> testHabLst = new ArrayList<Habit>();
    Calendar c = Calendar.getInstance();
    int[] freq = {0,0,0,0,0,0,0};
    Habit habit1 = new Habit("Walk dog", "fat Dog", c, freq, true, null);
    User followee = new User("john");
    User follower = new User("Hana");
    User requester = new User("Lol");

    HabitEvent event1 = new HabitEvent(date, "Did something", 0,"DId this");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        testHabLst.add(habit1);
        fire.setRequest(requester);
        fire.getRequestList(requesters);



    }

    public void createDocument(View view){
        fire.setHabit(habit1);
        fire.setFollowees(followee);
        fire.setFollowers(follower);
        fire.setRequest(requester);
    }

    public void readDocument(View view){
//        fire.getRequestList(requesters);
        Toast.makeText(this,requesters.get(0).getUsername(), Toast.LENGTH_SHORT).show();

    }








    private void startLogin(){
        startActivity(new Intent(TestActivity.this, MainActivity.class));
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int item_id = item.getItemId();

        if (item_id == R.id.logout){
            Toast.makeText(this, "Logging Out...", Toast.LENGTH_SHORT).show();
            AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                startLogin();
                            }
                            else{
                                Log.e(TAG, "onComplete", task.getException());
                            }
                        }
                    });
        }

        return true;
    }


    /* To check if there is a signed in user */
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().removeAuthStateListener(this);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null){
            startLogin();
            return;
        }

        firebaseAuth.getCurrentUser().getIdToken(true)
                .addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
                    @Override
                    public void onSuccess(GetTokenResult getTokenResult) {
                        Log.d(TAG, "onSuccess" + getTokenResult.getToken());
                    }
                });
    }

    @Override
    public void callHabitList(ArrayList<Habit> habits) {

    }

    @Override
    public void callRequestList(ArrayList<User> requesters) {

    }
}