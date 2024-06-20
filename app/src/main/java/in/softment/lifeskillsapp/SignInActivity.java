package in.softment.lifeskillsapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

import in.softment.lifeskillsapp.Model.UserModel;
import in.softment.lifeskillsapp.Util.ProgressHud;
import in.softment.lifeskillsapp.Util.Services;

public class SignInActivity extends AppCompatActivity {

    EditText emailAddress, password;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager mCallbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_in);
        emailAddress = findViewById(R.id.emailAddress);
        password = findViewById(R.id.password);

        //Reset Btn Clicked
        findViewById(R.id.resetPassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sEmail = emailAddress.getText().toString().trim();

                if (sEmail.isEmpty()) {
                    Services.showCenterToast(SignInActivity.this,"Enter Email Address");
                }
                else {
                    ProgressHud.show(SignInActivity.this,"Processing...");
                    FirebaseAuth.getInstance().sendPasswordResetEmail(sEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            ProgressHud.dialog.dismiss();
                            if (task.isSuccessful()) {
                                Services.showDialog(SignInActivity.this,"RESET PASSWORD","We have sent reset password link on your mail address.");
                            }
                            else {
                                Services.showDialog(SignInActivity.this,"ERROR",task.getException().getLocalizedMessage());
                            }
                        }
                    });
                }
            }
        });

        //RegisterBtnClicked
        findViewById(R.id.signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this,RegistrationActivity.class));
            }
        });

        //Google SignIn
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        findViewById(R.id.google).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 909);

            }
        });

        mCallbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>()
                {
                    @Override
                    public void onSuccess(LoginResult loginResult)
                    {
                        AuthCredential credential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
                        firebaseAuth(credential);

                    }

                    @Override
                    public void onCancel()
                    {

                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception)
                    {

                        Services.showDialog(SignInActivity.this,"ERROR",exception.getLocalizedMessage());
                    }


                });

        //Apple Login
        findViewById(R.id.facebook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(SignInActivity.this, Arrays.asList( "email", "public_profile"));



            }
        });

        //LoginBtnClicked
        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sEmail = emailAddress.getText().toString().trim();
                String sPassword = password.getText().toString().trim();

                if (sEmail.isEmpty()) {
                    Services.showCenterToast(SignInActivity.this,"Enter Email Address");
                }
                else if (sPassword.isEmpty()) {
                    Services.showCenterToast(SignInActivity.this,"Enter Password");
                }
                else {
                    ProgressHud.show(SignInActivity.this,"Sign In...");
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(sEmail, sPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            ProgressHud.dialog.dismiss();
                            if (task.isSuccessful()) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user != null) {

                                        Services.getCurrentUserData(SignInActivity.this,user.getUid(),true);

                                }
                            }
                            else {
                                Services.showDialog(SignInActivity.this,"ERROR",task.getException().getLocalizedMessage());
                            }
                        }
                    });
                }
            }
        });

        if (ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            });




    private void firebaseAuth(AuthCredential credential) {

        ProgressHud.show(this,"");
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        ProgressHud.dialog.dismiss();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            if (FirebaseAuth.getInstance().getCurrentUser() != null){
                                FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            if (task.getResult() != null && task.getResult().exists()) {
                                                Services.getCurrentUserData(SignInActivity.this,FirebaseAuth.getInstance().getCurrentUser().getUid(),true);
                                            }
                                            else {

                                                String emailId = "";
                                                for (UserInfo firUserInfo : FirebaseAuth.getInstance().getCurrentUser().getProviderData()){
                                                    emailId = firUserInfo.getEmail();
                                                }

                                                UserModel userModel = new UserModel();
                                                userModel.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                userModel.email = emailId;
                                                userModel.fullName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                                                userModel.registredAt = new Date();
                                                userModel.regiType = "google";


                                                Services.addUserDataOnServer(SignInActivity.this,userModel,false);
                                            }
                                        }
                                        else {
                                            Services.showDialog(SignInActivity.this,"ERROR",task.getException().getLocalizedMessage());
                                        }
                                    }
                                });

                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Services.showDialog(SignInActivity.this,"ERROR", Objects.requireNonNull(task.getException()).getLocalizedMessage());
                        }
                    }
                });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 909) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                firebaseAuth(credential);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Services.showDialog(SignInActivity.this,"ERROR",e.getLocalizedMessage());
            }
        }
        else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }


    }
}
