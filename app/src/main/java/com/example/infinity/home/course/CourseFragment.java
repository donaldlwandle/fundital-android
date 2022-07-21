package com.example.infinity.home.course;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.infinity.CourseGetterViewModel;
import com.example.infinity.Models.database.Courses;
import com.example.infinity.R;
import com.example.infinity.Utils.DownloadUtil;
import com.example.infinity.Utils.Methods;
import com.example.infinity.Utils.TabsPagerAdapter;
import com.example.infinity.VideoPlayerViewModel;
import com.example.infinity.discussion.DiscussionForum;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceFactory;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CourseFragment extends Fragment  {

    private static final String TAG = "CourseFragment";

    /*Widgets*/
    private View view;
    private TabLayout subjectTabs ;
    private ViewPager viewPager;
    private ImageView shareButton , playerBtnFullScreen , exoBackButton;
    private LinearLayout viewReviews;
    private TextView toolBarTitle , courseTitle , courseRatingsNO;
    private CircleImageView institutionLogo ;
    private RoundedImageView courseWallPaper;
    private RatingBar courseRating;
    private FloatingActionButton newDiscussionButton;
    private PlayerView playerView;
    private ProgressBar progressBar ;
    private ConstraintLayout videoPlayerContainer;
    private RelativeLayout courseDetailsContainer ,nestedScrollView  ;
    private ShimmerFrameLayout courseShimmerLayout;
    private AppBarLayout appBarLayout;
    private CoordinatorLayout coordinatorLayout ;

    /*adapter*/
    private TabsPagerAdapter pagerAdapter;

    /*Vars*/
    private Courses courseValue;
    private boolean fullScreen = false;
    private  String videoLink;

    /*libraries*/
    private SimpleExoPlayer simpleExoPlayer;


    /*Firebase*/
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_course, container ,false);

        initialize();
        setupViewPager();





        return view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SetUpVideoPlayingViewModel();

        /// get the course id
        getCourseIDsFromParent();



        /*sharing the current subject*/
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
                shareIntent.putExtra(Intent.EXTRA_TEXT,"Please checkout this Great Lessons on #"
                        +courseTitle.getText() + ", by #" +  toolBarTitle.getText() +" \n"
                        + " \n" +"https://play.google.com/store/apps/details?id=com.newton.application.newton&hl=en_AU");
                shareIntent.setType("text/plain");

                Intent sendIntent = Intent.createChooser(shareIntent ,null);
                requireActivity().startActivity(sendIntent);

            }
        });

        //view reviews
        viewReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.reviews_action);
            }
        });

        newDiscussionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FirebaseUser currentUser = mAuth.getCurrentUser();
                if (Methods.checkCurrentUser(currentUser)){
                    Navigation.findNavController(view).navigate(R.id.add_discussion_graph_action);
                }else {

                    Navigation.findNavController(view).navigate(R.id.login_flow_action);
                }

            }
        });


    }




    private void initialize(){
        /*back navigation*/
        ImageView backButton = view.findViewById(R.id.subject_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().onBackPressed();
            }
        });



        shareButton = view.findViewById(R.id.subject_share_btn);
        courseTitle = view.findViewById(R.id.course_title);
        toolBarTitle = view.findViewById(R.id.toolbar_course_title);
        courseRatingsNO = view.findViewById(R.id.course_ratings_number);
        institutionLogo = view.findViewById(R.id.course_logo);
        courseWallPaper = view.findViewById(R.id.wallpaper);
        subjectTabs = view.findViewById(R.id.subject_main_tabs);
        viewPager = view.findViewById(R.id.subject_main_container);
        viewReviews = view.findViewById(R.id.view_reviews);
        appBarLayout = view.findViewById(R.id.add_discussion_rel1);
        nestedScrollView = view.findViewById(R.id.nested_scroll_view);
        courseShimmerLayout = view.findViewById(R.id.course_shimmer_layout);
        coordinatorLayout = view.findViewById(R.id.coordinator);
        courseRating = view.findViewById(R.id.course_star_ratings);
        exoBackButton = view.findViewById(R.id.btn_back);

        exoBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });

        hideSystemUi();


        courseShimmerLayout.startShimmer();


        newDiscussionButton = view.findViewById(R.id.add_new_discussion_button);
        newDiscussionButton.hide();

        //EXO PLAYER
        playerView = view.findViewById(R.id.player_view);
        progressBar = view.findViewById(R.id.video_streaming_progress_bar);
        playerBtnFullScreen = view.findViewById(R.id.btn_full_screen);

        //PLayer and wallpaper container layout
        videoPlayerContainer = view.findViewById(R.id.video_player_container);
        courseDetailsContainer = view.findViewById(R.id.course_details_container);


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference() ;


        if (Methods.checkCurrentUser(mAuth.getCurrentUser())){


            if (!Methods.isDownloadsEmpty(getContext())){
                // downloads folder is empty

                try {
                    myRef.child("users_private_data")
                            .child(mAuth.getUid())
                            .child("downloaded_courses").setValue(null);

                }catch (NullPointerException e){
                    Log.e(TAG, "onBindViewHolder: NullPointerException : "  + e.getMessage());
                }

            }

        }
    }

    /*method for setting viewpager*/
    private void setupViewPager(){

        pagerAdapter =new TabsPagerAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        pagerAdapter.addFragments(new CourseInfoFragment());
        pagerAdapter.addFragments(new WatchFragment());
        pagerAdapter.addFragments(new DiscussionForum());

        viewPager.setAdapter(pagerAdapter);

        subjectTabs.setupWithViewPager(viewPager);
        subjectTabs.getTabAt(0).setIcon(R.drawable.info_selector);
        subjectTabs.getTabAt(1).setIcon(R.drawable.study_selector);
        subjectTabs.getTabAt(2).setIcon(R.drawable.discussion_selector);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2){
                    newDiscussionButton.show();
                }else {
                    newDiscussionButton.hide();
                }

                if (position == 0){
                    courseDetailsContainer.setVisibility(View.VISIBLE);
                    videoPlayerContainer.setVisibility(View.GONE);


                    if (simpleExoPlayer != null){
                        // stop video when ready
                        simpleExoPlayer.setPlayWhenReady(false);

                    }


                }else {
                    courseDetailsContainer.setVisibility(View.GONE);
                    videoPlayerContainer.setVisibility(View.VISIBLE);


                    if (simpleExoPlayer != null){

                        //get playback state
                        simpleExoPlayer.getPlaybackState();
                        // stop video when ready
                        simpleExoPlayer.setPlayWhenReady(true);
                        simpleExoPlayer.play();

                    }

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {



            }
        });


    }



    private void populateCourseDetails(){
        //set title
        courseTitle.setText(courseValue.getTitle());

        //set institution to toolbar
        toolBarTitle.setText(courseValue.getInstitution());



        //set ratings
        courseRating.setRating(Float.parseFloat(courseValue.getRating()));


        //set logo
        Glide
                .with(getActivity())
                .load(courseValue.getLogo())
                .placeholder(R.drawable.user_colored)
                .into(institutionLogo);

        //set course wallpaper
        Glide
                .with(getActivity())
                .load(courseValue.getWallpaper())
                .placeholder(R.drawable.infinity_profile)
                .into(courseWallPaper);


        courseDetailsContainer.setVisibility(View.VISIBLE);


        courseShimmerLayout.stopShimmer();
        courseShimmerLayout.setShimmer(null);
        courseShimmerLayout.setVisibility(View.GONE);

    }


    /*method for setting up exoplayer*/
    private void setupExoPlayer(){


        //initialize simple exoplayer
        simpleExoPlayer = new SimpleExoPlayer.Builder(requireContext()).build();



        /*set Player*/
        playerView.setPlayer(simpleExoPlayer);
        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
        simpleExoPlayer.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);

        /*play setup current  media*/
        if (videoLink != null && simpleExoPlayer != null){

            /*initialize Media source*/

            CacheDataSource.Factory introCacheDataSourceFactory = new CacheDataSource.Factory()
                    .setCache(DownloadUtil.getDownloadCache(getContext()))
                    .setUpstreamDataSourceFactory(new DefaultDataSourceFactory(getContext(), new DefaultHttpDataSource.Factory()))
                    .setCacheWriteDataSinkFactory(null)
                    .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR);

            ProgressiveMediaSource introMediaSource =
                    new ProgressiveMediaSource.Factory(introCacheDataSourceFactory)
                            .createMediaSource(MediaItem.fromUri(Uri.parse(videoLink)));




            /*set media item to the player*/
            simpleExoPlayer.setMediaSource(introMediaSource);
            simpleExoPlayer.prepare();
            simpleExoPlayer.setPlayWhenReady(false);

        }




        /*add listener to the exoplayer*/
        simpleExoPlayer.addListener(new Player.Listener(){
            @Override
            public void onPlaybackStateChanged(int state) {
                if (state == Player.STATE_BUFFERING){
                    /*show progress bar when buffering*/
                    progressBar.setVisibility(View.VISIBLE);

                }else if (state == Player.STATE_READY){
                    /*hide ProgressBar when ready*/
                    progressBar.setVisibility(View.GONE);
                }

            }
        });





        playerBtnFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check flag

                if (fullScreen){
                    playerBtnFullScreen.setImageResource(R.drawable.exo_controls_fullscreen_enter);


                    /*set portrait mode*/
                    requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                    /*show system UI*/
                    nestedScrollView.setVisibility(View.VISIBLE);

                    /*make playerView original  screen*/
                    CoordinatorLayout.LayoutParams portraitParams = (CoordinatorLayout.LayoutParams)appBarLayout.getLayoutParams() ;
                    portraitParams.width = CoordinatorLayout.LayoutParams.MATCH_PARENT ;
                    portraitParams.height = (int)(300 * requireContext().getResources().getDisplayMetrics().density);
                    appBarLayout.setLayoutParams(portraitParams);


                    /*set flag value to false*/
                    fullScreen= false;

                }else {
                    //when flag is false
                    playerBtnFullScreen.setImageResource(R.drawable.exo_controls_fullscreen_exit);

                    /*set land scape mode*/
                    requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                    /*hide system UI*/
                    nestedScrollView.setVisibility(View.GONE);

                    /*make playerView full screen*/
                    CoordinatorLayout.LayoutParams landScapeParams = (CoordinatorLayout.LayoutParams)appBarLayout.getLayoutParams() ;
                    landScapeParams.width = CoordinatorLayout.LayoutParams.MATCH_PARENT ;
                    landScapeParams.height = CoordinatorLayout.LayoutParams.MATCH_PARENT ;
                    appBarLayout.setLayoutParams(landScapeParams);

                    /*set flag value to true*/
                    fullScreen = true ;
                }

            }
        });



    }

    private void playVideo(){

        if (videoLink != null && simpleExoPlayer != null){

            /*initialize Media source*/

            CacheDataSource.Factory cacheDataSourceFactory = new CacheDataSource.Factory()
                    .setCache(DownloadUtil.getDownloadCache(getContext()))
                    .setUpstreamDataSourceFactory(new DefaultDataSourceFactory(getContext(), new DefaultHttpDataSource.Factory()))
                    .setCacheWriteDataSinkFactory(null)
                    .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR);

            ProgressiveMediaSource mediaSource =
                    new ProgressiveMediaSource.Factory(cacheDataSourceFactory)
                            .createMediaSource(MediaItem.fromUri(Uri.parse(videoLink)));




            /*set media item to the player*/
            simpleExoPlayer.setMediaSource(mediaSource);
            simpleExoPlayer.prepare();
            simpleExoPlayer.setPlayWhenReady(true);
            simpleExoPlayer.play();

        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        playerView.setPlayer(null);
        if (simpleExoPlayer != null){
            simpleExoPlayer.setPlayWhenReady(false);
            simpleExoPlayer.release();
            simpleExoPlayer = null ;

        }

        showSystemUi();
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


    }

    @Override
    public void onStop() {
        super.onStop();


    }

    @Override
    public void onResume() {
        super.onResume();
        /*set portrait mode*/
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    private void SetUpVideoPlayingViewModel(){
        /*View models*/
        VideoPlayerViewModel videoPlayerViewModel = new ViewModelProvider(requireActivity()).get(VideoPlayerViewModel.class);
        videoPlayerViewModel.getVideoUrl().observe(getViewLifecycleOwner(), new Observer<CharSequence>() {
            @Override
            public void onChanged(CharSequence charSequence) {
                videoLink = charSequence.toString();
                playVideo();


            }
        });
    }

    /* Getting course ID's From LiveData View Model updated when navigating to the current course
     * View Model is CourseGetterViewModel*/
    private void getCourseIDsFromParent(){
        CourseGetterViewModel courseGetterViewModel = new ViewModelProvider(requireActivity()).get(CourseGetterViewModel.class);
        courseGetterViewModel.getCourse().observe(getViewLifecycleOwner(), new Observer<Courses>() {
            @Override
            public void onChanged(Courses courses) {
                courseValue = courses ;
                videoLink = courses.getIntro_vid();
                setupExoPlayer();

                Log.d(TAG, "onChanged: The new Video Url is ++++++++++" + videoLink);
                populateCourseDetails();


            }
        });

    }

    /*method for hiding system ui*/
    @SuppressLint("InlinedApi")
    private void showSystemUi() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            final WindowInsetsController controller = requireActivity().getWindow().getInsetsController();

            if (controller != null)
                controller.show(WindowInsets.Type.statusBars());
        }
        else {

            requireActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }


    /*method for hiding system ui*/
    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

    }

    /*Another method for hiding the system UI*/
    private void hideSystemUiAlt(){

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            final WindowInsetsController controller = requireActivity().getWindow().getInsetsController();

            if (controller != null)
                controller.hide(WindowInsets.Type.statusBars());
        }
        else {
            //noinspection deprecation
            requireActivity().getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }*/

    }

    /*this method goes with the above hideSystemUiAlt for showing back the systemUi and it must be called on the onDestroyedView instance of fragment life cycle*/
    private void showSystemUiAlt(){
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            final WindowInsetsController controller = requireActivity().getWindow().getInsetsController();

            if (controller != null)
                controller.show(WindowInsets.Type.statusBars());
        }
        else {
            //noinspection deprecation
            requireActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }*/
    }




}
