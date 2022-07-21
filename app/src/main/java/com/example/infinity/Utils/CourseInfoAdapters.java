package com.example.infinity.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.infinity.Models.database.courseinfo.CourseInstructors;
import com.example.infinity.Models.database.courseinfo.CourseSkills;
import com.example.infinity.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CourseInfoAdapters {
    private static final String TAG = "CourseInfoAdapters";



    /******************************************************** SKILLS ADAPTER ********************************************************************/
    public static class CourseSkillsAdapter extends RecyclerView.Adapter<CourseSkillsAdapter.CourseSkillsViewHolder>{

        private Context mContext ;
        private List<CourseSkills> list;

        public CourseSkillsAdapter(Context mContext, List<CourseSkills> list) {
            this.mContext = mContext;
            this.list = list;
        }

        @NonNull
        @Override
        public CourseSkillsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_course_skills_item , parent ,false);
            return new CourseSkillsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CourseSkillsViewHolder holder, int position) {

            CourseSkills courseSkill = list.get(position);
            holder.skill_txt.setText(courseSkill.SKILL);

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        /*View Holder*/
        public static class CourseSkillsViewHolder extends RecyclerView.ViewHolder{
            TextView skill_txt;

            public CourseSkillsViewHolder(@NonNull View itemView) {
                super(itemView);
                skill_txt = itemView.findViewById(R.id.skills_txt);
            }
        }
    }



    /******************************************************** INSTRUCTORS ADAPTER ********************************************************************/
    public static class CourseInstructorsAdapter extends RecyclerView.Adapter<CourseInstructorsAdapter.CourseInstructorsViewHolder>{

        private Context mContext ;
        private List<CourseInstructors> list;

        public CourseInstructorsAdapter(Context mContext, List<CourseInstructors> list) {
            this.mContext = mContext;
            this.list = list;
        }


        @NonNull
        @Override
        public CourseInstructorsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_course_instructors_item , parent ,false);
            return new CourseInstructorsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CourseInstructorsViewHolder holder, int position) {
            CourseInstructors courseInstructors = list.get(position);

            //set instructor name
            holder.instructor.setText(courseInstructors.getName());

            //set instructor profile pic
            Glide
                    .with(mContext)
                    .load(courseInstructors.getProfile_pic())
                    .placeholder(R.drawable.user_colored)
                    .into(holder.profilePic);

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        /*View Holder*/
        public static class CourseInstructorsViewHolder extends RecyclerView.ViewHolder{
            TextView instructor ;
            CircleImageView profilePic;

            public CourseInstructorsViewHolder(@NonNull View itemView) {
                super(itemView);
                instructor = itemView.findViewById(R.id.instructor_name);
                profilePic = itemView.findViewById(R.id.instructor_pp);
            }
        }
    }

}
