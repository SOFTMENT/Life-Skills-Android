package in.softment.lifeskillsapp.Fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import in.softment.lifeskillsapp.Adapter.VideoAdapter;
import in.softment.lifeskillsapp.Model.VideoModel;
import in.softment.lifeskillsapp.R;
import in.softment.lifeskillsapp.Util.ProgressHud;
import in.softment.lifeskillsapp.Util.Services;


public class VideosFragment extends Fragment {

    private TextView no_videos_available;
    private VideoAdapter videoAdapter;
    private ArrayList<VideoModel> videoModels;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_videos, container, false);

        videoModels = new ArrayList<>();
        no_videos_available = view.findViewById(R.id.no_videos_available);
        videoAdapter = new VideoAdapter(getContext(), videoModels);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(videoAdapter);
        getAllVideos();
        return view;
    }

    public void getAllVideos(){

        FirebaseFirestore.getInstance().collection("Videos").orderBy("title").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error == null) {
                    videoModels.clear();
                    if (value != null && !value.isEmpty()) {
                        for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                            VideoModel videoModel = documentSnapshot.toObject(VideoModel.class);
                            videoModels.add(videoModel);
                        }
                    }
                    if (videoModels.size() > 0) {
                        no_videos_available.setVisibility(View.GONE);
                    }
                    else {
                        no_videos_available.setVisibility(View.VISIBLE);
                    }
                    videoAdapter.notifyDataSetChanged();
                }
                else {
                    Services.showDialog(getContext(),"ERROR",error.getLocalizedMessage());
                }
            }
        });
    }


}
