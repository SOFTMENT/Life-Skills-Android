package in.softment.lifeskillsapp.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import in.softment.lifeskillsapp.Model.DailyMessageModel;
import in.softment.lifeskillsapp.Model.UserModel;
import in.softment.lifeskillsapp.R;
import in.softment.lifeskillsapp.Util.ProgressHud;
import in.softment.lifeskillsapp.Util.Services;


public class QuoteFragment extends Fragment {

    private TextView day, month, year, title, quote;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quote, container, false);

        view.findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                /*This will be the actual content you wish you share.*/
                String shareBody = title.getText().toString()+"\n\n"+quote.getText().toString();
                /*The type of the content is text, obviously.*/
                intent.setType("text/plain");
                /*Applying information Subject and Body.*/
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareBody);
                intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                /*Fire!*/
                startActivity(Intent.createChooser(intent, "Share Using"));
            }
        });

        day = view.findViewById(R.id.day);
        month = view.findViewById(R.id.month);
        year = view.findViewById(R.id.year);
        title = view.findViewById(R.id.title);
        quote = view.findViewById(R.id.quote);
        getDailyInsight();
        return view;
    }

    private void getDailyInsight() {
        ProgressHud.show(getContext(),"Loading...");
        int lastQuoteId = UserModel.data.getLastQuotesId();
        FirebaseFirestore.getInstance().collection("DailyInsights").whereGreaterThan("count",lastQuoteId).whereLessThan("count",lastQuoteId + 4)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ProgressHud.dialog.dismiss();
                        if (task.getResult() != null && !task.getResult().isEmpty()) {
                            DailyMessageModel dailyMessageModel = task.getResult().getDocuments().get(0).toObject(DailyMessageModel.class);
                            title.setText(dailyMessageModel.getTitle());
                            quote.setText(dailyMessageModel.getQuotes());
                            day.setText(Services.convertDateToDay(new Date()));
                            month.setText(Services.convertDateToMonth(new Date()));
                            year.setText(Services.convertDateToYear(new Date()));
                        }
                        else {
                            UserModel.data.lastQuotesId = 0;
                            Map<String, Integer> map = new HashMap();
                            map.put("lastQuotesId",0);
                            FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(map, SetOptions.merge());
                            getDailyInsight();
                        }
                    }
                });
    }
}
