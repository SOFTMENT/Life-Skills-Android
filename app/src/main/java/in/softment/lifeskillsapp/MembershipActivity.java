package in.softment.lifeskillsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.revenuecat.purchases.CustomerInfo;
import com.revenuecat.purchases.Offering;
import com.revenuecat.purchases.Offerings;
import com.revenuecat.purchases.Purchases;
import com.revenuecat.purchases.PurchasesError;
import com.revenuecat.purchases.interfaces.PurchaseCallback;
import com.revenuecat.purchases.interfaces.ReceiveCustomerInfoCallback;
import com.revenuecat.purchases.interfaces.ReceiveOfferingsCallback;
import com.revenuecat.purchases.models.StoreTransaction;

import in.softment.lifeskillsapp.Model.UserModel;
import in.softment.lifeskillsapp.Util.Constants;
import in.softment.lifeskillsapp.Util.ProgressHud;
import in.softment.lifeskillsapp.Util.Services;

public class MembershipActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_membership);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Services.logout(MembershipActivity.this);
            }
        });

        findViewById(R.id.purchaseBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressHud.show(MembershipActivity.this,"");
                Purchases.getSharedInstance().getOfferings(new ReceiveOfferingsCallback() {
                    @Override
                    public void onReceived(@NonNull Offerings offerings) {

                        Offering offering = offerings.getCurrent();
                        Purchases.getSharedInstance().purchasePackage(MembershipActivity.this, offering.getAvailablePackages().get(0), new PurchaseCallback() {
                            @Override
                            public void onCompleted(@NonNull StoreTransaction storeTransaction, @NonNull CustomerInfo customerInfo) {

                                Purchases.getSharedInstance().getCustomerInfo(new ReceiveCustomerInfoCallback() {
                                    @Override
                                    public void onReceived(@NonNull CustomerInfo customerInfo) {
                                        ProgressHud.dialog.dismiss();

                                        if (customerInfo.getEntitlements().get("Premium") != null && customerInfo.getEntitlements().get("Premium").isActive()) {
                                            Services.showCenterToast(MembershipActivity.this,"Subscription Purchased Successfully");
                                            Constants.expireDate = customerInfo.getEntitlements().get("Premium").getExpirationDate();
                                            UserModel.data.membership = true;
                                            FirebaseFirestore.getInstance().collection("Users").
                                                    document(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                                                    set(UserModel.data, SetOptions.merge());

                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Intent intent = new Intent(MembershipActivity.this, MainActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);

                                                }
                                            },1200);
                                        }
                                    }

                                    @Override
                                    public void onError(@NonNull PurchasesError purchasesError) {
                                        ProgressHud.dialog.dismiss();
                                        Services.showDialog(MembershipActivity.this,"ERROR",purchasesError.getMessage());
                                    }
                                });
                            }

                            @Override
                            public void onError(@NonNull PurchasesError purchasesError, boolean b) {
                                ProgressHud.dialog.dismiss();
                                Services.showDialog(MembershipActivity.this,"ERROR",purchasesError.getMessage());
                            }
                        });
                    }

                    @Override
                    public void onError(@NonNull PurchasesError purchasesError) {
                        ProgressHud.dialog.dismiss();
                        Services.showDialog(MembershipActivity.this,"ERROR",purchasesError.getMessage());
                    }
                });


            }
        });

        findViewById(R.id.restore).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ProgressHud.show(MembershipActivity.this,"Restoring...");
                Purchases.getSharedInstance().restorePurchases(new ReceiveCustomerInfoCallback() {
                    @Override
                    public void onReceived(@NonNull CustomerInfo customerInfo) {
                        ProgressHud.dialog.dismiss();
                        if (customerInfo.getEntitlements().get("Premium") != null && customerInfo.getEntitlements().get("Premium").isActive()) {
                            Constants.expireDate = customerInfo.getEntitlements().get("Premium").getExpirationDate();
                            Intent intent  = new Intent(MembershipActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        else {
                            Services.showCenterToast(MembershipActivity.this,"No active subscription found.");
                        }
                    }

                    @Override
                    public void onError(@NonNull PurchasesError purchasesError) {
                        ProgressHud.dialog.dismiss();
                        Services.showDialog(MembershipActivity.this,"ERROR",purchasesError.getMessage());
                    }


                });
            }
        });

        findViewById(R.id.privacyPolicy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://softment.in/privacy-policy/"));
                startActivity(browserIntent);

            }
        });

        findViewById(R.id.termsOfService).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://softment.in/terms-of-service/"));
                startActivity(browserIntent);

            }
        });
    }
}
