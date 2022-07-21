package com.example.infinity.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.infinity.Models.database.Replies;
import com.example.infinity.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RepliesAdapter extends RecyclerView.Adapter<RepliesAdapter.RepliesViewHolder> {

    /*tools*/
    private final List<Replies> repliesList;
    private final Context mContext;



    public RepliesAdapter(List<Replies> repliesList, Context mContext) {
        this.repliesList = repliesList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RepliesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_reply_item , parent , false);



        return new RepliesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RepliesViewHolder holder, int position) {

        holder.reply = repliesList.get(position);

        //set review username
        holder.replyUserName.setText(holder.reply.getUsername());

        //set review profile pic
        Glide
                .with(mContext)
                .load(holder.reply.getProfile_pic())
                .placeholder(R.drawable.user_colored)
                .into(holder.profileImage);

        //set review time
        //set time
        String timeDifference = Methods.getTimeStampDifference(holder.reply.getDate_created());
        if (timeDifference.equals("0")){
            holder.replyDate.setText("today");
        }else if (timeDifference.equals("1")){
            holder.replyDate.setText("a day ago");
        }else {
            holder.replyDate.setText(timeDifference + " days ago");
        }

        //set review
        if (holder.reply.getReply()!= null){
            holder.replyText.setText(holder.reply.getReply());
        }


        if (holder.reply.getInstructor().equals("yes")){
            holder.isConstructor.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return repliesList.size();
    }

    /*View Holder*/
    public static class RepliesViewHolder extends RecyclerView.ViewHolder{

        //widgets
        private CircleImageView profileImage ;
        private TextView replyUserName , replyText , replyDate , isConstructor;
        private Replies reply ;


        public RepliesViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.replies_profile_image);
            replyUserName = itemView.findViewById(R.id.replies_item_username);
            replyText = itemView.findViewById(R.id.replies_item_reply);
            replyDate = itemView.findViewById(R.id.reply_item_time);
            isConstructor = itemView.findViewById(R.id.is_reply_instructor);




        }
    }
}
