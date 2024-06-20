package in.softment.lifeskillsapp.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.revenuecat.purchases.CustomerInfo;
import com.revenuecat.purchases.Purchases;
import com.revenuecat.purchases.PurchasesError;
import com.revenuecat.purchases.interfaces.ReceiveCustomerInfoCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import in.softment.lifeskillsapp.MainActivity;
import in.softment.lifeskillsapp.MembershipActivity;
import in.softment.lifeskillsapp.Model.UserModel;
import in.softment.lifeskillsapp.R;
import in.softment.lifeskillsapp.SignInActivity;


public class Services {
    public static String getMonthName(int month) {
        switch (month) {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "Jun";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
            default:
                return "Failed";
        }
    }


        public static  String inputStreamToString(InputStream inputStream) {
        try {
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, bytes.length);
            String json = new String(bytes);
            return json;
        } catch (IOException e) {
            return null;
        }
    }
    public static boolean isPromoting(Date date){
        Date currentDate = new Date();
        if (currentDate.compareTo(date) < 0) {
            return true;
        }
        else {
            return false;
        }
    }

    public static  String convertDateToYear(Date date) {
        if (date == null) {
            date = new Date();
        }
        date.setTime(date.getTime());
        String pattern = "yyyy";
        DateFormat df = new SimpleDateFormat(pattern, Locale.getDefault());
        return  df.format(date);
    }


    public static  String convertDateToMonth(Date date) {
        if (date == null) {
            date = new Date();
        }
        date.setTime(date.getTime());
        String pattern = "MMM";
        DateFormat df = new SimpleDateFormat(pattern, Locale.getDefault());
        return  df.format(date);
    }

    public static String convertSecToMinAndSec(int sec) {


        int minutes = sec / 60;
        int seconds = sec % 60;
        return String.format("%02d : %02d",minutes, seconds);
    }

    public static  String convertDateToDay(Date date) {
        if (date == null) {
            date = new Date();
        }
        date.setTime(date.getTime());
        String pattern = "dd";
        DateFormat df = new SimpleDateFormat(pattern, Locale.getDefault());
        return  df.format(date);
    }
    public static  String convertDateToHourMin(Date date) {
        if (date == null) {
            date = new Date();
        }
        date.setTime(date.getTime());
        String pattern = "hh:mm a";
        DateFormat df = new SimpleDateFormat(pattern, Locale.getDefault());
        return  df.format(date);
    }
    public static  String convertDateToDateTimeString(Date date) {
        if (date == null) {
            date = new Date();
        }
        date.setTime(date.getTime());
        String pattern = "dd-MMM-yyyy, hh:mm a";
        DateFormat df = new SimpleDateFormat(pattern, Locale.getDefault());
        return  df.format(date);
    }

    public static void showCenterToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0,0);
        toast.show();
    }

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    private static Date currentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }
    public static String getTimeAgo(Date date) {
        long time = date.getTime();
        if (time < 1000000000000L) {
            time *= 1000;
        }

        long now = currentDate().getTime();
        if (time > now || time <= 0) {
            return "in the future";
        }

        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "moments ago";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 60 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 2 * HOUR_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }

    public static void logout(Context context) {

                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(context, SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);




    }


    public static void showDialog(Context context,String title,String message) {

        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(false);
            Activity activity = (Activity) context;
            View view = activity.getLayoutInflater().inflate(R.layout.error_message_layout, null);
            TextView titleView = view.findViewById(R.id.title);
            TextView msg = view.findViewById(R.id.message);
            titleView.setText(title);
            msg.setText(message);
            builder.setView(view);
            AlertDialog alertDialog = builder.create();
            view.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();


                }
            });

            if(!((Activity) context).isFinishing())
            {
                alertDialog.show();

            }
        }
        catch (Exception e) {
            Log.d("SOFTMENTERROR",e.getLocalizedMessage());
        }


    }

    public static void sentEmailVerificationLink(Context context){

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            ProgressHud.show(context,"");
            FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    ProgressHud.dialog.dismiss();

                    if (task.isSuccessful()) {
                        showDialog(context,"VERIFY YOUR EMAIL","We have sent verification link on your mail address.");
                    }
                    else {
                        showDialog(context,"ERROR",task.getException().getLocalizedMessage());
                    }
                }
            });
        }
        else {
            ProgressHud.dialog.dismiss();
        }

    }




    public static void addUserDataOnServer(Context context, UserModel userModel, boolean shouldMerge){

        ProgressHud.show(context,"");
        Task<Void> task = FirebaseFirestore.getInstance().collection("Users").document(userModel.getUid()).set(userModel);
        if (shouldMerge) {
            task = FirebaseFirestore.getInstance().collection("Users").document(userModel.getUid()).set(userModel, SetOptions.merge());
        }
        task.addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                ProgressHud.dialog.dismiss();
                if (task.isSuccessful()) {
                    Services.getCurrentUserData(context,FirebaseAuth.getInstance().getCurrentUser().getUid(),true);

                }
                else {
                    Services.showDialog(context,"ERROR",task.getException().getLocalizedMessage());
                }
            }
        });
    }


    public static void getCurrentUserData(Context context,String uid, Boolean showProgress) {

        if (showProgress) {
            ProgressHud.show(context,"");
        }

        FirebaseFirestore.getInstance().collection("Users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {


                if (showProgress) {
                    ProgressHud.dialog.dismiss();
                }

                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        documentSnapshot.toObject(UserModel.class);

                        if (UserModel.data != null) {

                            Purchases.getSharedInstance().getCustomerInfo(new ReceiveCustomerInfoCallback() {
                                @Override
                                public void onReceived(@NonNull CustomerInfo customerInfo) {

                                    if (customerInfo.getEntitlements().get("Premium") != null && customerInfo.getEntitlements().get("Premium").isActive()) {
                                        Constants.expireDate = customerInfo.getEntitlements().get("Premium").getExpirationDate();
                                        Intent intent  = new Intent(context, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(intent);
                                    }
                                    else {
                                        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equalsIgnoreCase("NhKpaoZpDvXEM2g2mJptod5nGgJ2")) {
                                            Intent intent  = new Intent(context, MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            context.startActivity(intent);
                                        }
                                        else {
                                            Intent intent  = new Intent(context, MembershipActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            context.startActivity(intent);
                                        }

                                    }
                                }

                                @Override
                                public void onError(@NonNull PurchasesError purchasesError) {

                                    Intent intent  = new Intent(context, MembershipActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intent);
                                }
                            });

                        }
                        else  {
                            showCenterToast(context,"Something Went Wrong. Code - 101");
                        }
                    }
                    else {
                        FirebaseAuth.getInstance().getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    showDialog(context,"User Not Found","Your account is not available. Please create your account.");
                                }
                                else {
                                    showDialog(context,"ERROR",task.getException().getLocalizedMessage());
                                }
                            }
                        });
                    }
                }
                else {
                    FirebaseAuth.getInstance().getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                showDialog(context,"User Not Found","Your account is not available. Please create your account.");
                            }
                            else {
                                showDialog(context,"ERROR",task.getException().getLocalizedMessage());
                            }
                        }
                    });
                }

            }
        });
    }





}
