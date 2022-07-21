package com.example.infinity.Utils;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.mbms.DownloadStatusListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infinity.MediaDownloadService;
import com.example.infinity.Models.StringMagic;
import com.example.infinity.Models.database.MediaItems;
import com.example.infinity.R;
import com.example.infinity.VideoPlayerViewModel;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.offline.DefaultDownloadIndex;
import com.google.android.exoplayer2.offline.Download;
import com.google.android.exoplayer2.offline.DownloadManager;
import com.google.android.exoplayer2.offline.DownloadRequest;
import com.google.android.exoplayer2.offline.DownloadService;
import com.google.android.exoplayer2.offline.ProgressiveDownloader;
import com.google.android.exoplayer2.scheduler.Scheduler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import static com.google.android.exoplayer2.util.Assertions.checkNotNull;

public class VideosItemsAdapter extends RecyclerView.Adapter<VideosItemsAdapter.VideosItemsViewHolder> {

    private static final String TAG = "VideosItemsAdapter";
    /*vars*/
    private List<MediaItems> list ;
    private Context mContext ;
    private String courseID ;
    private String chapterID ;

    private StringMagic stringMagic = new StringMagic() ;



    /*firebase*/
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private Query query ;

    /*View Models*/
    private VideoPlayerViewModel playerViewModel ;


    public VideosItemsAdapter(List<MediaItems> list, Context mContext, String courseID, String chapterID) {
        this.list = list;
        this.mContext = mContext;
        this.courseID = courseID;
        this.chapterID = chapterID;
    }

    @NonNull
    @Override
    public VideosItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_media_list_item , parent , false);

        playerViewModel = new ViewModelProvider((ViewModelStoreOwner) mContext).get(VideoPlayerViewModel.class);
        myRef = FirebaseDatabase.getInstance().getReference() ;
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getUid() != null){

            query = myRef.child("users_private_data")
                    .child(mAuth.getUid())
                    .child("downloaded_courses")
                    .child(courseID)
                    .child("chapters")
                    .orderByKey().equalTo(chapterID);

        }



        return new VideosItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideosItemsViewHolder holder, int position) {
        final MediaItems mediaItem = list.get(position);
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        if (Methods.checkCurrentUser(mAuth.getCurrentUser())){

            try {

                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){

                            if (dataSnapshot.child(chapterID).child("downloaded_media").getValue().toString().contains(String.valueOf(position))){
                                holder.downloadMediaButton.setVisibility(View.GONE);
                                holder.downloadingMediaTxt.setVisibility(View.GONE);
                                holder.downloadedMediaTxt.setVisibility(View.VISIBLE);
                            };

                            stringMagic.setStringUpdate(dataSnapshot.child(chapterID).child("downloaded_media").getValue().toString());


                        }else {
                            stringMagic.setStringUpdate("");
                            holder.downloadMediaButton.setVisibility(View.VISIBLE);
                            holder.downloadingMediaTxt.setVisibility(View.GONE);
                            holder.downloadedMediaTxt.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }catch (NullPointerException e){
                Log.e(TAG, "onBindViewHolder: NullPointerException : "  + e.getMessage());
            }

        }










        /*setting media description*/
        holder.mediaDescription.setText(mediaItem.getMedia_description());

        /*setting subscription type*/
        if (mediaItem.getSubscription_type().equalsIgnoreCase("free")){
            holder.subscriptionTypeImage.setVisibility(View.GONE);
            holder.freeOrEnrollmentTxt.setVisibility(View.VISIBLE);


        }else {
            holder.subscriptionTypeImage.setVisibility(View.VISIBLE);
            holder.freeOrEnrollmentTxt.setVisibility(View.GONE);


        }

        /*setting media type*/
        if (mediaItem.getMedia_type().equalsIgnoreCase("video")){
            holder.fileTypeImage.setImageResource(R.drawable.youtube);
            holder.downloadMediaButton.setVisibility(View.VISIBLE);
            holder.docOrTimeTxt.setText("04:10 min");


        }else {

            holder.fileTypeImage.setImageResource(R.drawable.google_docs);
            holder.downloadMediaButton.setVisibility(View.GONE);
            holder.docOrTimeTxt.setText("document");

        }


        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Methods.checkCurrentUser(currentUser)){

                    playerViewModel.setVideoUrl(mediaItem.getMedia_url());

                    if (mediaItem.getMedia_type().equalsIgnoreCase("document")){

                        Navigation.findNavController(view).navigate(R.id.pdf_streaming_action);

                    }
                    /*playerViewModel.setVideoUrl("https://drive.google.com/uc?id=18nMea0FAMfH591b2p9YnGiwi09Gti7gU");*/

                }else {
                    Navigation.findNavController(view).navigate(R.id.login_flow_action);
                }



            }
        });

        holder.downloadMediaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Methods.checkCurrentUser(currentUser)){

                    DownloadRequest downloadRequest = new DownloadRequest.Builder(DownloadServArgs.DOWNLOAD_INDEX_ID ,Uri.parse(mediaItem.getMedia_url())).build();
                    ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                    if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                        //we are connected to a network

                        DownloadService.sendAddDownload(
                                mContext,
                                MediaDownloadService.class,
                                downloadRequest,
                                /* foreground= */ false);

                        holder.downloadMediaButton.setVisibility(View.GONE);
                        holder.downloadingMediaTxt.setVisibility(View.VISIBLE);
                        holder.downloadedMediaTxt.setVisibility(View.GONE);

                        DownloadUtil.getDownloadManager(mContext).addListener(new DownloadManager.Listener() {
                            @Override
                            public void onInitialized(DownloadManager downloadManager) {


                            }

                            @Override
                            public void onDownloadsPausedChanged(DownloadManager downloadManager, boolean downloadsPaused) {

                            }

                            @Override
                            public void onDownloadChanged(DownloadManager downloadManager, Download download, @Nullable Exception finalException) {

                                if (download.state == Download.STATE_COMPLETED){

                                    try {

                                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                Methods.updateChaptersDownloadedVideos(stringMagic.getStringUpdate().concat(String.valueOf(position)) ,mAuth.getUid(), courseID ,chapterID , myRef);
                                                /*holder.downloadMediaButton.setVisibility(View.GONE);*/
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                    }catch (NullPointerException e){
                                        Log.e(TAG, "onBindViewHolder: NullPointerException : "  + e.getMessage());
                                    }

                                }

                            }
                        });


                    }else {
                        Toast.makeText(mContext , "Check your network connection and try again." , Toast.LENGTH_LONG).show();
                    }





                }else {

                    Navigation.findNavController(view).navigate(R.id.login_flow_action);
                }

            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    /* Adapter */
    public static class VideosItemsViewHolder extends RecyclerView.ViewHolder{
        ImageView fileTypeImage , subscriptionTypeImage , downloadMediaButton , downloadedMediaTxt;
        TextView docOrTimeTxt , freeOrEnrollmentTxt ,mediaDescription  ;
        ProgressBar downloadingMediaTxt ;
        View parent ;


        public VideosItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView;
            fileTypeImage =  itemView.findViewById(R.id.file_type);
            subscriptionTypeImage = itemView.findViewById(R.id.subscription_type);
            docOrTimeTxt = itemView.findViewById(R.id.doc_or_time);
            freeOrEnrollmentTxt = itemView.findViewById(R.id.free_txt);
            mediaDescription = itemView.findViewById(R.id.media_tittle);
            downloadMediaButton = itemView.findViewById(R.id.download_media_button);
            downloadedMediaTxt = itemView.findViewById(R.id.downloaded_media_button);
            downloadingMediaTxt = itemView.findViewById(R.id.downloading_media_button);
        }
    }


}
