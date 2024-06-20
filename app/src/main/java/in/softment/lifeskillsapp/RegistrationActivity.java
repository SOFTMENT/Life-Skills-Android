package in.softment.lifeskillsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;

import in.softment.lifeskillsapp.Model.UserModel;
import in.softment.lifeskillsapp.Util.ProgressHud;
import in.softment.lifeskillsapp.Util.Services;

public class RegistrationActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registration);

        EditText firstName = findViewById(R.id.fullName);
        EditText emailAddress = findViewById(R.id.emailAddress);
        EditText password = findViewById(R.id.password);

        //BACK
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String sFirst = firstName.getText().toString().trim();
                String sEmail = emailAddress.getText().toString().trim();
                String sPassword = password.getText().toString().trim();



                if (sFirst.isEmpty()) {
                    Services.showCenterToast(RegistrationActivity .this,"Enter Full Name Name");
                }
                else if (sEmail.isEmpty()) {
                    Services.showCenterToast(RegistrationActivity .this,"Enter Email Address");
                }
                else if (sPassword.isEmpty()) {
                    Services.showCenterToast(RegistrationActivity .this,"Enter Password");
                }
                else {
                    ProgressHud.show(RegistrationActivity .this,"Creating Account...");
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(sEmail, sPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            ProgressHud.dialog.dismiss();
                            if (task.isSuccessful()) {
                                UserModel userModel = new UserModel();
                                userModel.email = sEmail;
                                userModel.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                userModel.fullName = sFirst;
                                userModel.registredAt = new Date();
                                userModel.regiType = "custom";
                                Services.addUserDataOnServer(RegistrationActivity .this,userModel,false);

                            }
                            else {
                                ProgressHud.dialog.dismiss();
                                Services.showDialog(RegistrationActivity .this,"ERROR",task.getException().getLocalizedMessage());
                            }
                        }
                    });
                }
            }
        });

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

}
