/**
 * This class is able to upload desired data to the firebase, and get the current user uid
 * Author: Katia Zhang, Frank Li
 */



package com.example.happyhabitapp;

import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FireBase {
    private final String TAG = "FireBase";

    private ArrayList<Habit> habitList;
    private ArrayList<User> followerLst = new ArrayList<>();
    private ArrayList<User> followeeLst;
    private ArrayList<HabitEvent> eventLst;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String current_uid;

    CollectionReference Users = db.collection("Users");
    DocumentReference User = Users.document(getUserName());
    CollectionReference HabitList = User.collection("HabitList");
    CollectionReference Followers = User.collection("Followers");
    CollectionReference Followees = User.collection("Followees");



    /* Constructors */
    public FireBase() {}

    /* setters */

    /**
     * this function upload user's information to firebase
     * @param user (User class object)
     */

    public void setUser(User user) {
        /*
        Map<String, Object> map = new HashMap<>();

        map.put("Current_uid", user.getCurrent_uid());
        map.put("Name", user.getUsername());
        map.put("Pic_path", user.getPicPath());
        map.put("habits", user.getHabitList());
        map.put("followers", user.getFollowList());

         */

        User
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: user added to fire");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: could not add user", e);
                    }
                });
                //.add(user)
                //.add(map)



    }

    /**
     * this function upload habits to the firebase
     * @param habit (Habit class object)
     */

    public void setHabit(Habit habit) {
        List<Integer> freq = new ArrayList<Integer>();
        for (int i = 0; i < (habit.getWeek_freq().length); i++){
            freq.add(habit.getWeek_freq()[i]);
        }
        Map<String, Object> map = new HashMap<>();

        map.put("Title", habit.getTitle());
        map.put("Reason", habit.getReason());
        map.put("Days", freq);
        map.put("Dates", habit.getDate());


        HabitList
                .document(habit.getTitle())
                .update(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: added habit to fire");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: could not add habit to fire", e);
                    }
                });

    }

    /**
     * this function upload information about followers to firebase
     * @param followers (User class object)
     */

    public void setFollowers(User followers) {
        Followers
                .document(followers.getUsername())
                .set(followers)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: added follower to fire");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: could not add follower", e);
                    }
                });
    }

    /**
     * this function upload information about followees
     * @param followees (User class object)
     */

    public void setFollowees(User followees) {
        Followees
                .document(followees.getUsername())
                .set(followees)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: added followees to fire");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: could not add followees", e);
                    }
                });
    }

    /**
     * this function will upload habit event to the firebase
     * @param event (HabitEvent class object)
     */

    public void setHabitEventEvent(HabitEvent event) {

        List<Integer> freq = new ArrayList<Integer>();
        for (int i = 0; i < (event.getWeek_freq().length); i++){
            freq.add(event.getWeek_freq()[i]);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("Title", event.getTitle());
        map.put("Reason", event.getReason());
        map.put("Days", freq);
        map.put("Dates", event.getDate());
        map.put("About", event.getAbout());
        HabitList
                .document(event.getTitle())
                .set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: added event to fire");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: could not add event", e);
                    }
                });
    }


    /* getters */

    /**
     * This function will return the current userid
     * @return
     */
    public String getCurrent_uid() {
        current_uid =  FirebaseAuth.getInstance().getCurrentUser().getUid();
        return current_uid;
    }

    /* get information: this feature is not working */

    /**
     * this function return current user's user name as string
     * @return
     */
    public String getUserName(){
        String current_name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        return current_name;
    }


    /**
     * this function returns follower list
     * @return
     */
    public ArrayList<User> getFollowerLst(){
        Followers
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        followerLst.clear();
                        for (QueryDocumentSnapshot doc: value){
                            Log.d(TAG, "onEvent: getting followers");
                            User follower = doc.toObject(com.example.happyhabitapp.User.class);
                            followerLst.add(follower);
                        }
                    }
                });
        return followerLst;
    }




}

