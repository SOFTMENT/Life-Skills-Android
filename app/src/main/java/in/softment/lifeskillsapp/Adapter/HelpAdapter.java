package in.softment.lifeskillsapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.softment.lifeskillsapp.Model.ContactModel;
import in.softment.lifeskillsapp.R;

public class HelpAdapter extends RecyclerView.Adapter<HelpAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ContactModel> contactModels;
    public HelpAdapter(Context context, ArrayList<ContactModel> contactModels){
        this.context = context;
        this.contactModels = contactModels;
    }

    @NonNull
    @Override
    public HelpAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.help_center_view_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull HelpAdapter.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        ContactModel contactModel = contactModels.get(position);
        holder.webUrl.setText(contactModel.getMessage());
        holder.title.setText(contactModel.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contactModel.getMessage().contains("@")) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("plain/text");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[] { contactModel.getMessage() });
                    context.startActivity(Intent.createChooser(intent, ""));
                }
                else {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://"+contactModel.getMessage()));
                    context.startActivity(browserIntent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title, webUrl;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            webUrl = itemView.findViewById(R.id.webUrl);
        }
    }
}
