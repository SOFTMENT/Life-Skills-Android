package in.softment.lifeskillsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

import in.softment.lifeskillsapp.Adapter.HelpAdapter;
import in.softment.lifeskillsapp.Model.ContactModel;
import in.softment.lifeskillsapp.Util.ProgressHud;
import in.softment.lifeskillsapp.Util.Services;

public class HelpCenterActivity extends AppCompatActivity {
    private HelpAdapter helpAdapter;
    private ArrayList<ContactModel> contactModels;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_helpcenter);
        contactModels = new ArrayList<>();
        helpAdapter = new HelpAdapter(this, contactModels);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(helpAdapter);

        getAllContact();
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void getAllContact() {
        ProgressHud.show(this,"");
        FirebaseFirestore.getInstance().collection("Contacts").orderBy("createDate", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ProgressHud.dialog.dismiss();
                if (task.isSuccessful()) {
                    contactModels.clear();
                    if (task.getResult() != null && !task.getResult().isEmpty()) {
                        for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                            ContactModel contactModel = documentSnapshot.toObject(ContactModel.class);
                            contactModels.add(contactModel);
                        }
                    }
                    helpAdapter.notifyDataSetChanged();
                }
                else {
                    Services.showDialog(HelpCenterActivity.this,"ERROR",task.getException().getLocalizedMessage());
                }
            }
        });
    }

}
