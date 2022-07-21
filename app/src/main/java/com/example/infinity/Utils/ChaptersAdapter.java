package com.example.infinity.Utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infinity.Models.database.Chapters;
import com.example.infinity.Models.database.MediaItems;
import com.example.infinity.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChaptersAdapter extends RecyclerView.Adapter<ChaptersAdapter.VideoChaptersViewHolder> {
    private static final String TAG = "ChaptersAdapter";

    private List<Chapters> chaptersList ;
    private Context mContext ;

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

    /*adapters*/
    private VideosItemsAdapter videosItemsAdapter ;

    public ChaptersAdapter(List<Chapters> chaptersList, Context mContext) {
        this.chaptersList = chaptersList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public VideoChaptersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_item_video_chapter , parent ,false);

        myRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();




        return new VideoChaptersViewHolder(view);
    }




    @Override
    public void onBindViewHolder(@NonNull VideoChaptersViewHolder holder, int position) {

        Chapters chapter = chaptersList.get(position);
        String chapterName  ="Chapter " + String.valueOf(position + 1) + " . " +chapter.getChapter_title() ;


        //set chapter name
        holder.chapterTitle.setText(chapterName);



        try {

            myRef.child("institutions_courses").child(chapter.getInstitution_id())
                    .child(chapter.getCourse_id())
                    .child("chapters")
                    .child(chapter.getChapter_id())
                    .child("media")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            /*display video topics*/
                            List<MediaItems> videoItemsList = new ArrayList<>();

                            for (DataSnapshot singleSnapShot : dataSnapshot.getChildren()){
                                videoItemsList.add(singleSnapShot.getValue(MediaItems.class));
                            }

                            videosItemsAdapter = new VideosItemsAdapter(videoItemsList ,  mContext ,chapter.getCourse_id() ,chapter.getChapter_id());

                            holder.videoListContainer.setLayoutManager(new LinearLayoutManager(mContext));
                            holder.videoListContainer.setHasFixedSize(true);

                            holder.videoListContainer.setAdapter(videosItemsAdapter);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

        }catch (NullPointerException e){
            Log.e(TAG, "onBindViewHolder: NullPointerException : " + e.getMessage() );
        }



    }

    @Override
    public int getItemCount() {
        return chaptersList.size();
    }

    /*ViewHolder*/
    public static class VideoChaptersViewHolder extends RecyclerView.ViewHolder{

        RecyclerView videoListContainer ;
        TextView chapterTitle;

        public VideoChaptersViewHolder(@NonNull View itemView) {
            super(itemView);

            videoListContainer = itemView.findViewById(R.id.media_list_container);
            chapterTitle = itemView.findViewById(R.id.chapter_name);
        }
    }



}
