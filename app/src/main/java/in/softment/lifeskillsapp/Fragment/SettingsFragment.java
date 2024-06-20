package in.softment.lifeskillsapp.Fragment;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.Date;

import in.softment.lifeskillsapp.BuildConfig;
import in.softment.lifeskillsapp.HelpCenterActivity;
import in.softment.lifeskillsapp.Model.UserModel;
import in.softment.lifeskillsapp.R;
import in.softment.lifeskillsapp.Util.Constants;
import in.softment.lifeskillsapp.Util.Services;

public class SettingsFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        view.findViewById(R.id.shareApp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Life Skills App");
                    String shareMessage= "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }
            }
        });
        view.findViewById(R.id.rateApp).setOnClickListener(view13 -> {
            Uri uri = Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID);
            Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(myAppLinkToMarket);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getContext(), " unable to find market app", Toast.LENGTH_LONG).show();
            }
        });


        TextView daysLeft = view.findViewById(R.id.goPremiumText);
        long diff = Constants.expireDate.getTime() - new Date().getTime();
        int numOfDays = (int) (diff / (1000 * 60 * 60 * 24));
        if (numOfDays > 1) {
            daysLeft.setText((numOfDays + 1) +" Days left");
        }
        else {
           daysLeft.setText((numOfDays + 1)+" Day left");
        }


        view.findViewById(R.id.privacy_policy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://softment.in/privacy-policy/"));
                startActivity(browserIntent);
            }
        });

        view.findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Services.logout(getContext());
            }
        });

        view.findViewById(R.id.help_cener).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), HelpCenterActivity.class));
            }
        });

        view.findViewById(R.id.termsAndConditions).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://softment.in/terms-of-service/"));
                startActivity(browserIntent);
            }
        });

        //VersionCode
        TextView version = view.findViewById(R.id.version);
        String versionName = BuildConfig.VERSION_NAME;
        version.setText(versionName);


        TextView name = view.findViewById(R.id.name);
        TextView email = view.findViewById(R.id.email);


        name.setText(UserModel.data.getFullName());
        email.setText(UserModel.data.getEmail());


        return view;
    }
}
