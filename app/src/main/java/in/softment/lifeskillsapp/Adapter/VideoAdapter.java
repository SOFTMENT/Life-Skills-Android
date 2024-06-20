package in.softment.lifeskillsapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

import in.softment.lifeskillsapp.Model.VideoModel;
import in.softment.lifeskillsapp.PlayLandscapeVideoActivity;
import in.softment.lifeskillsapp.R;
import in.softment.lifeskillsapp.Util.Services;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private Context context;
    private ArrayList<VideoModel> videoModels;

    public VideoAdapter(Context context, ArrayList<VideoModel> videoModels) {
        this.context = context;
        this.videoModels = videoModels;
    }

    @NonNull
    @Override
    public VideoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.video_layout_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull VideoAdapter.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        VideoModel videoModel = videoModels.get(position);
        holder.title.setText(videoModel.title);
        holder.duration.setText(Services.convertSecToMinAndSec(videoModel.duration));
        Glide.with(context).load(videoModel.getThumbnail()).placeholder(R.drawable.placeholder).into(holder.videoImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlayLandscapeVideoActivity.class);
                intent.putExtra("link",videoModel.getVideoUrl());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return videoModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title, duration;
        private RoundedImageView videoImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            duration = itemView.findViewById(R.id.duration);
            videoImage = itemView.findViewById(R.id.videoImage);
        }
    }
}
