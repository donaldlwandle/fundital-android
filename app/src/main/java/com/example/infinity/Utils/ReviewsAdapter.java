package com.example.infinity.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.infinity.Models.database.Reviews;
import com.example.infinity.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {

    /*tools*/
    private final List<Reviews> reviewsList;
    private final Context mContext;

    public ReviewsAdapter(List<Reviews> reviewsList, Context mContext) {
        this.reviewsList = reviewsList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_review_item , parent , false);
        return new ReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsViewHolder holder, int position) {

        Reviews review = reviewsList.get(position);

        //set review username
        holder.reviewUserName.setText(review.getUsername());

        //set review profile pic
        Glide
                .with(mContext)
                .load(review.getProfile_pic())
                .placeholder(R.drawable.user_colored)
                .into(holder.profileImage);

        //set review time
        //set time
        String timeDifference = Methods.getTimeStampDifference(review.getDate_created());
        if (timeDifference.equals("0")){
            holder.reviewDate.setText("today");
        }else if (timeDifference.equals("1")){
            holder.reviewDate.setText("a day ago");
        }else {
            holder.reviewDate.setText(timeDifference + " days ago");
        }

        //set review
        if (review.getReview() != null){
            holder.reviewText.setText(review.getReview());
        }


        //set rating
        if (review.getRating() != null){
            holder.ratingBar.setRating(Float.parseFloat(review.getRating()));
        }

    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

    /*View Holder*/
    public static class ReviewsViewHolder extends RecyclerView.ViewHolder{

        //widgets
        private CircleImageView profileImage ;
        private TextView reviewUserName , reviewText , reviewDate;
        private RatingBar ratingBar ;

        public ReviewsViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.reviews_profile_image);
            reviewUserName = itemView.findViewById(R.id.reviews_item_username);
            reviewText = itemView.findViewById(R.id.reviews_item_review);
            reviewDate = itemView.findViewById(R.id.review_item_time);
            ratingBar = itemView.findViewById(R.id.review_item_rating_bar);

        }
    }
}
