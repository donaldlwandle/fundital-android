package com.example.infinity.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.infinity.DiscussionDCarierModel;
import com.example.infinity.Models.database.Discussions;
import com.example.infinity.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DiscussionsAdapter extends RecyclerView.Adapter<DiscussionsAdapter.DiscussionsViewHolder> {

    private static final String TAG = "DiscussionsAdapter";

    private final List<Discussions> list;
    private  final Context mContext ;
    private final String mInstitutionId , mCourseId ;

    /*view models*/
    private DiscussionDCarierModel idCarierModel ;


    private final FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance() ;
    private final DatabaseReference myRef = mFirebaseDatabase.getReference();


    public DiscussionsAdapter(List<Discussions> list, Context mContext, String mInstitutionId, String mCourseId) {
        this.list = list;
        this.mContext = mContext;
        this.mInstitutionId = mInstitutionId;
        this.mCourseId = mCourseId;
    }

    @NonNull
    @Override
    public DiscussionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_item_discussion , parent , false);

        idCarierModel = new ViewModelProvider((ViewModelStoreOwner)mContext).get(DiscussionDCarierModel.class);


        return new DiscussionsViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull DiscussionsViewHolder holder, int position) {
        holder.mDiscussion = list.get(position);




        Query query = myRef.child("institutions_courses")
                .child(mInstitutionId)
                .child(mCourseId)
                .child("discussions")
                .child(holder.mDiscussion.getDiscussion_id())
                .child("votes");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                // Discussion has votes already
                if (dataSnapshot.exists()){

                    if (Methods.checkCurrentUser(FirebaseAuth.getInstance().getCurrentUser())){
                        holder.mLikedByUser = Methods.isLikedByCurrentUser(dataSnapshot);
                    }else {
                        holder.mLikedByUser = false ;
                    }




                    //no votes in the discussion
                }else{
                    holder.mLikedByUser = false;


                }

                holder.mGestureDetector = new GestureDetector(mContext, new VotesGestureListener(holder.mVote,mInstitutionId ,mCourseId ,holder.mDiscussion.getDiscussion_id(), holder.mLikedByUser));

                if (holder.mLikedByUser){
                    holder.mGreenVoteButton.setVisibility(View.VISIBLE);
                    holder.mGreyVoteButton.setVisibility(View.GONE);
                    holder.mGreenVoteButton.setOnTouchListener((v, event) -> {
                        Log.d(TAG, "onTouch: green vote touched");

                        if (Methods.checkCurrentUser(FirebaseAuth.getInstance().getCurrentUser())){


                            return holder.mGestureDetector.onTouchEvent(event);

                        }else {
                            Navigation.findNavController(v).navigate(R.id.login_flow_action);
                            return false;
                        }


                    });


                }else {

                    holder.mGreenVoteButton.setVisibility(View.GONE);
                    holder.mGreyVoteButton.setVisibility(View.VISIBLE);
                    holder.mGreyVoteButton.setOnTouchListener((v, event) -> {
                        Log.d(TAG, "onTouch: grey vote touched");

                        if (Methods.checkCurrentUser(FirebaseAuth.getInstance().getCurrentUser())){
                            return holder.mGestureDetector.onTouchEvent(event);

                        }else {
                            Navigation.findNavController(v).navigate(R.id.login_flow_action);
                            return false ;
                        }

                    });
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });









        /*set instructor indicator in if the user is an instructor*/
        if (holder.mDiscussion.getInstructor().equals("yes")){
            holder.isInstructor.setVisibility(View.VISIBLE);
        }else {
            holder.isInstructor.setVisibility(View.GONE);
        }

        //set username
        holder.username.setText(holder.mDiscussion.getUsername());

        //set profile Picture

        Glide
                .with(mContext)
                .load(holder.mDiscussion.getProfile_pic())
                .placeholder(R.drawable.user_colored)
                .into(holder.profileImage);

        //set time
        String timeDifference = Methods.getTimeStampDifference(holder.mDiscussion.getDate_created());
        if (timeDifference.equals("0")){
            holder.time.setText("today");
        }else if (timeDifference.equals("1")){
            holder.time.setText("a day ago");
        }else {
            holder.time.setText(timeDifference + " days ago");
        }

        //set discussion
        holder.discussion.setText(holder.mDiscussion.getDiscussion());



        //set number of upVotes ;
        if (holder.mDiscussion.getVotes() != null){
            holder.upVotes.setText(String.valueOf(holder.mDiscussion.getVotes().size()));

        }


        // navigating to replies Fragment
        holder.seeRepliesText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idCarierModel.setDiscussionID(holder.mDiscussion.getDiscussion_id());
            }
        });





    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /*View holder*/
    public static class DiscussionsViewHolder extends RecyclerView.ViewHolder{

        /*Widgets*/
        private CircleImageView profileImage;
        private ImageView mGreyVoteButton , mGreenVoteButton ;
        private TextView username , time ,discussion ,votesText , upVotes , seeRepliesText, isInstructor;
        private Discussions mDiscussion ;
        private GestureDetector mGestureDetector ;
        private Vote mVote ;
        private Boolean mLikedByUser;

        public DiscussionsViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.discussion_photo);
            username = itemView.findViewById(R.id.discussion_user_name);
            time =itemView.findViewById(R.id.discussion_time);
            discussion= itemView.findViewById(R.id.discussion_text);
            upVotes = itemView.findViewById(R.id.up_votes_text);
            seeRepliesText = itemView.findViewById(R.id.view_all_replies);
            isInstructor = itemView.findViewById(R.id.is_instructor);
            mGreenVoteButton = itemView.findViewById(R.id.vote_button);
            mGreyVoteButton = itemView.findViewById(R.id.dis_vote_button);

            mVote = new Vote(mGreenVoteButton , mGreyVoteButton);



        }
    }
}
