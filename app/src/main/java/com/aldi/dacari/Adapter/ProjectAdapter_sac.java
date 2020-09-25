package com.aldi.dacari.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aldi.dacari.Activity.ProjectActivity;
import com.aldi.dacari.Model.Project;
import com.aldi.dacari.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class ProjectAdapter_sac extends RecyclerView.Adapter<ProjectAdapter_sac.ViewHolder> {

    private Context mContext;
    private List<Project> mProjects;

    public ProjectAdapter_sac(Context mContext, List<Project> mProjects, boolean b){
        this.mContext = mContext;
        this.mProjects = mProjects;
    }

    @NonNull
    @Override
    public ProjectAdapter_sac.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.project_item, parent, false);
        return new ProjectAdapter_sac.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectAdapter_sac.ViewHolder holder, int position) {
        final Project project = mProjects.get(position);
        holder.title.setText(project.getTitle());
        holder.time.setText(project.getTime());
        holder.budget.setText(project.getBudget());
        holder.type.setText("Servis AC");

        holder.pengalaman.setText(project.getTrack_record());

        Glide.with(mContext).load(project.getImage()).into(holder.image_post);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ProjectActivity.class);
                intent.putExtra("projectid", project.getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mProjects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView title, time, bids, budget, type, pengalaman;
        ImageView image_post;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.project_title);
            time = itemView.findViewById(R.id.period_project);
            budget = itemView.findViewById(R.id.cost);
            type = itemView.findViewById(R.id.typep);
            image_post = itemView.findViewById(R.id.image_post_upload);

            pengalaman = itemView.findViewById(R.id.pengalaman_kerja);

        }
    }
}