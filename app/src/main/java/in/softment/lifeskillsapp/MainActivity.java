package in.softment.lifeskillsapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import in.softment.lifeskillsapp.Fragment.QuoteFragment;
import in.softment.lifeskillsapp.Fragment.SettingsFragment;
import in.softment.lifeskillsapp.Fragment.VideosFragment;
import in.softment.lifeskillsapp.Model.UserModel;
import in.softment.lifeskillsapp.Util.NonSwipeAbleViewPager;

public class MainActivity extends AppCompatActivity {
    private NonSwipeAbleViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private ImageView quoteImage, videoImage, settingsImage;
    private RelativeLayout quoteBtn, videoBtn, settingsBtn;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                } else {
                    // TODO: Inform user that that your app will not show notifications.
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //ViewPager
        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(5);

        quoteImage = findViewById(R.id.quoteImage);
        videoImage = findViewById(R.id.videoImage);
        settingsImage = findViewById(R.id.settingsImage);


        findViewById(R.id.quoteBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPagerItem(0);
            }
        });

        findViewById(R.id.videoBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPagerItem(1);
            }
        });


        findViewById(R.id.settingsBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPagerItem(2);
            }
        });




        setPagerItem(0);

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()) {
                    UserModel.data.notiToken = task.getResult();
                    FirebaseFirestore.getInstance().collection("Users")
                            .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .set(UserModel.data, SetOptions.merge());
                }
            }
        });


      askNotificationPermission();
    }




    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch( android.Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }





    public void setPagerItem(int item){


       quoteImage.setImageResource(R.drawable.quote);
        videoImage.setImageResource(R.drawable.play);
        settingsImage.setImageResource(R.drawable.settings);


        if (item == 0) {

            quoteImage.setImageResource(R.drawable.quotered);
        }
        else if (item == 1) {

            videoImage.setImageResource(R.drawable.playred);
        }
        else if (item == 2) {

            settingsImage.setImageResource(R.drawable.settingred);
        }


        viewPager.setCurrentItem(item);
    }
    private void setupViewPager(ViewPager viewPager) {

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFrag(new QuoteFragment());
        viewPagerAdapter.addFrag(new VideosFragment());
        viewPagerAdapter.addFrag(new SettingsFragment());
        viewPager.setAdapter(viewPagerAdapter);

    }

    static class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();


        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {


            return mFragmentList.get(position);
        }

        @Override
        public int getItemPosition(@NonNull @NotNull Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {

            return mFragmentList.size();
        }



        public void addFrag(Fragment fragment) {
            mFragmentList.add(fragment);

        }
    }



}
