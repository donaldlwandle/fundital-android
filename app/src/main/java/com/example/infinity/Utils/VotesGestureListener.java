package com.example.infinity.Utils;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import androidx.annotation.NonNull;

import com.example.infinity.Models.database.Votes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class VotesGestureListener extends GestureDetector.SimpleOnGestureListener {
    private static final String TAG = "VotesGestureListener";

    private final Vote vote;
    private String institutionID , courseId , discussionId;
    private Boolean isLikedByCurrentUser ;

    /*firebase*/
    private final FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance() ;
    private final DatabaseReference myRef = mFirebaseDatabase.getReference();



    public VotesGestureListener(Vote vote, String institutionID, String courseId, String discussionId, Boolean isLikedByCurrentUser) {
        this.vote = vote;
        this.institutionID = institutionID;
        this.courseId = courseId;
        this.discussionId = discussionId;
        this.isLikedByCurrentUser = isLikedByCurrentUser;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }


    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.d(TAG, "onSingleTapUp: single tap detected");

        initiateVote();

        return true;
    }



    private void initiateVote(){

        Query query = myRef.child("institutions_courses")
                .child(institutionID)
                .child(courseId)
                .child("discussions")
                .child(discussionId)
                .child("votes");


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Discussion has votes already
                if (dataSnapshot.exists()){

                    for (DataSnapshot singleSnapShot : dataSnapshot.getChildren()){

                        // Case 1 : The User Already up voted the Photo
                        if (isLikedByCurrentUser &&  singleSnapShot.getValue(Votes.class).getUser_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                            myRef.child("institutions_courses")
                                    .child(institutionID)
                                    .child(courseId)
                                    .child("discussions")
                                    .child(discussionId)
                                    .child("votes")
                                    .child(singleSnapShot.getKey()).removeValue();
                            vote.toggleVote();


                            // Case 2 : !case1
                        }else if(!isLikedByCurrentUser){

                            //add new like to the discussion
                            addNewLike();
                            break;

                        }



                    }


                    //no votes in the discussion
                }else {
                    addNewLike();

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void addNewLike(){
        Log.d(TAG, "addNewLike: Adding new Vote.");
        Votes vVote =new Votes();


        vVote.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());

        myRef.child("institutions_courses")
                .child(institutionID)
                .child(courseId)
                .child("discussions")
                .child(discussionId)
                .child("votes")
                .child(myRef.push().getKey()).setValue(vVote);






    }
    
}
