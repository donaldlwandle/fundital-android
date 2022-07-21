package com.example.infinity.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.infinity.CourseGetterViewModel;
import com.example.infinity.Models.database.Courses;
import com.example.infinity.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public  class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.SubjectsViewHolder>{


    private Courses course;

    private ArrayList<Courses> list;
    private Context mContext ;
    private CourseGetterViewModel courseGetterViewModel ;




    public CoursesAdapter(ArrayList<Courses> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public CoursesAdapter.SubjectsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_item_subjects , parent , false);

        courseGetterViewModel = new ViewModelProvider((ViewModelStoreOwner) mContext).get(CourseGetterViewModel.class);

        return new CoursesAdapter.SubjectsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoursesAdapter.SubjectsViewHolder holder, int position) {

        course = list.get(position);

        //set course wallpaper
        Glide
                .with(mContext)
                .load(course.getWallpaper())
                .into(holder.courseWallpaper);

        //set course title
        holder.courseTitle.setText(course.getTitle());

        //set institution name
        holder.courseInstitution.setText(course.getInstitution());

        //set course views
        holder.courseViews.setText(course.getSeen());


        //set course ratings
        holder.ratingBar.setNumStars(5);
        holder.ratingBar.setRating(Float.parseFloat(course.getRating()));



        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                courseGetterViewModel.setCourse(course);

                Navigation.findNavController(view).navigate(R.id.subjectAction);


            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /*view holder*/
    public static class SubjectsViewHolder extends RecyclerView.ViewHolder{
        /*widgets*/
        RelativeLayout subjectView;
        RoundedImageView courseWallpaper ;
        TextView courseTitle , courseInstitution , courseViews ;
        RoundedImageView wallpaperOpaque ;
        RatingBar ratingBar ;


        View parent ;

        public SubjectsViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView ;
            subjectView = itemView.findViewById(R.id.subject_item_rel1);
            courseWallpaper= itemView.findViewById(R.id.course_wallpaper);
            courseTitle = itemView.findViewById(R.id.course_title);
            courseInstitution = itemView.findViewById(R.id.course_institution);
            courseViews = itemView.findViewById(R.id.course_views);
            ratingBar = itemView.findViewById(R.id.course_ratings);
            wallpaperOpaque = itemView.findViewById(R.id.image_opaque_background);
        }
    }
}
