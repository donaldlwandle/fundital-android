package com.example.infinity.Utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.infinity.R;

import java.util.ArrayList;

public class GridImageAdapter extends RecyclerView.Adapter<GridImageAdapter.GridViewHolder> {
    private static final String TAG = "GridImageAdapter";

    /*interfaces*/
    public interface OnItemClickedListener{
        void onItemClicked(int position);
    }

    private Context mContext ;
    private ArrayList<String> imagesList ;
    private OnItemClickedListener mOnItemClickedListener;

    public GridImageAdapter(Context mContext, ArrayList<String> imagesList , OnItemClickedListener onItemClickedListener) {
        this.mContext = mContext;
        this.imagesList = imagesList;
        this.mOnItemClickedListener = onItemClickedListener;
    }

    @NonNull
    @Override
    public GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_grid_image , parent ,false);

        return new GridViewHolder(view , mOnItemClickedListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final GridViewHolder holder, int position) {

        String imageUrl = imagesList.get(position);
        holder.progressBar.setVisibility(View.VISIBLE);

        Glide
                .with(mContext)
                .load(imageUrl)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                        holder.progressBar.setVisibility(View.GONE);

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.displayImage);


    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    /*viewHolder*/
    public static class GridViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /*Widgets*/
        SquareImageView displayImage;
        ProgressBar progressBar ;

        /**/
        OnItemClickedListener onItemClickedListener;

        public GridViewHolder(@NonNull View itemView , OnItemClickedListener onItemClickedListener) {
            super(itemView);

            displayImage = itemView.findViewById(R.id.grid_img_upload);
            progressBar = itemView.findViewById(R.id.grid_img_progressbar);
            this.onItemClickedListener = onItemClickedListener;

            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            onItemClickedListener.onItemClicked(getAdapterPosition());
        }
    }


}
