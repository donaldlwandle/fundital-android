package com.example.infinity.home.course;

import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.infinity.R;
import com.example.infinity.VideoPlayerViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import es.voghdev.pdfviewpager.library.RemotePDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;
import es.voghdev.pdfviewpager.library.remote.DownloadFile;
import es.voghdev.pdfviewpager.library.util.FileUtil;

public class PdfDisplayFragment extends Fragment  implements DownloadFile.Listener {

    private static final String TAG = "PdfDisplayFragment";

    /*widgets*/
    private View view;
    private LinearLayout contentView ;
    private ProgressBar progressBar ;

    /*Libraries*/

    /*Vars*/
    private RemotePDFViewPager remotePDFViewPager ;
    private PDFPagerAdapter adapter ;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pdf_display , container , false);
        initialize();


        return view ;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SetUpMediaPlayingViewModel();

    }

    private void initRenderer(String url) {

        remotePDFViewPager =
                new RemotePDFViewPager(getContext(), url, this);

    }

    @Override
    public void onStop() {
        super.onStop();

        if (adapter != null){
            adapter.close();
        }
    }

    /*method for initializing  widgets*/
    private  void initialize(){
        contentView = view.findViewById(R.id.pdf_layout);
        progressBar = view.findViewById(R.id.login_progress_bar);
        progressBar.setVisibility(View.VISIBLE);


    }

    @Override
    public void onSuccess(String url, String destinationPath) {

        adapter = new PDFPagerAdapter(getContext(), FileUtil.extractFileNameFromURL(url));
        remotePDFViewPager.setAdapter(adapter);
        contentView.addView(remotePDFViewPager,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        progressBar.setVisibility(View.GONE);

    }

    @Override
    public void onFailure(Exception e) {

        Toast.makeText(getContext(), "Opening document failed , try again later.", Toast.LENGTH_SHORT).show();
        requireActivity().onBackPressed();

    }

    @Override
    public void onProgressUpdate(int progress, int total) {

    }


    private void SetUpMediaPlayingViewModel(){
        /*View models*/
        VideoPlayerViewModel videoPlayerViewModel = new ViewModelProvider(requireActivity()).get(VideoPlayerViewModel.class);
        videoPlayerViewModel.getVideoUrl().observe(getViewLifecycleOwner(), new Observer<CharSequence>() {
            @Override
            public void onChanged(CharSequence charSequence) {
                initRenderer(charSequence.toString());
            }
        });
    }
}
