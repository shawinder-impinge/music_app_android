package com.musicapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.musicapp.R;
import com.musicapp.adapter.FreeTrialAdapter;
import com.musicapp.adapter.MeditationAdapter;
import com.musicapp.adapter.MostPopularAdapter;
import com.musicapp.adapter.SubscriptionAdapter;
import com.musicapp.util.Constants;
import com.musicapp.util.Utility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.lang.Long.parseLong;

public class SubscriptionActivity extends BaseActivity implements View.OnClickListener, PurchasesUpdatedListener {
    Context context = this;
    private TextView subscribe;
    RecyclerView subscription_recycler, freetrial_benifit_recycler;
    SubscriptionAdapter subscriptionAdapter;
    FreeTrialAdapter freeTrialAdapter;
    private ImageView cross;
    Dialog mDialog;
    private boolean isSubValid = false;
    private Boolean allowBackpress = true;
    public static final String ITEM_SKU = "lt2017.12mo";
    public static final String ITEM_SKU_1MO = "lt2018.1mo";
    public static final String ITEM_SKU_3MO = "lt2018.3mo";
    BillingClient billingClient;
    private ArrayList<String> skuList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);
        fullWindow();
        findId();
        setAdapter();
        checkSubscription();
    }

    private void setAdapter() {

        freeTrialAdapter = new FreeTrialAdapter(context);
        freetrial_benifit_recycler.setAdapter(freeTrialAdapter);


        subscriptionAdapter = new SubscriptionAdapter(context);
        subscription_recycler.setAdapter(subscriptionAdapter);
    }

    private void findId() {
        subscription_recycler = findViewById(R.id.subscription_recycler);
        freetrial_benifit_recycler = findViewById(R.id.freetrial_benifit_recycler);
        subscribe = findViewById(R.id.subscribe);
        cross = findViewById(R.id.cross);
        cross.setOnClickListener(this);
        subscription_recycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        freetrial_benifit_recycler.setLayoutManager(new LinearLayoutManager(context));
        subscription_recycler.setHasFixedSize(true);
        freetrial_benifit_recycler.setHasFixedSize(true);
        subscribe.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cross:
                finish();
                break;
            case R.id.subscribe:
               // showSubscriptionDialognew();
                break;
        }
    }


    private void showSubscriptionDialognew() {
        mDialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setCancelable(true);
        if (mDialog.getWindow() != null) {
            mDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            mDialog.getWindow().setGravity(Gravity.CENTER);
            WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
            lp.dimAmount = 0.75f;
            mDialog.getWindow()
                    .addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mDialog.getWindow();
            mDialog.getWindow().setAttributes(lp);
            mDialog.setContentView(R.layout.dialog_subscription);

            final RadioButton rb_months_12 = mDialog.findViewById(R.id.rb_months_12);
            final RadioButton rb_months_3 = mDialog.findViewById(R.id.rb_months_3);
            final RadioButton rb_months_1 = mDialog.findViewById(R.id.rb_months_1);
            TextView save = mDialog.findViewById(R.id.save);
            TextView cancel = mDialog.findViewById(R.id.cancel);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDialog.dismiss();
                }
            });
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   /* if (rb_months_12.isChecked()) {
                        skuList.clear();
                        skuList.add(ITEM_SKU);

                    } else if (rb_months_3.isChecked()) {
                        skuList.clear();
                        skuList.add(ITEM_SKU_3MO);

                    } else if (rb_months_1.isChecked()) {
                        skuList.clear();
                        skuList.add(ITEM_SKU_1MO);

                    } else {

                    }
                    if (skuList.size() != 0) {
                        setupBillingClient();
                    }*/

                }
            });

            mDialog.show();
        }
    }


    private void setupBillingClient() {

        Log.e("STATUS", "setupBillingClient");

        billingClient = BillingClient
                .newBuilder(this)
                .setListener(this)
                .enablePendingPurchases()
                .build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {

                if (billingResult != null && billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {

                    Log.e("STATUS", "onBillingSetupFinished");

                    onLoadProductsClicked();
                } else {
                    Log.e("STATUS", "onBillingSetupFailed");

                }

            }

            @Override
            public void onBillingServiceDisconnected() {

            }
        });
    }

    private void onLoadProductsClicked() {

        Log.e("STATUS", "onLoadProductsClicked");

        if (billingClient.isReady()) {

            Log.e("STATUS", "isReady");

            SkuDetailsParams params = SkuDetailsParams
                    .newBuilder()
                    .setSkusList(skuList)
                    .setType(BillingClient.SkuType.SUBS)
                    .build();
            billingClient.querySkuDetailsAsync(params,
                    (billingResult, skuDetailsList) -> {
                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                            initProductAdapter(skuDetailsList);
                        } else {

                        }

                    });
        } else {

            Log.e("STATUS", "isNotReady");

        }
    }

    private void initProductAdapter(List<SkuDetails> skuDetailsList) {
        BillingFlowParams billingFlowParams = BillingFlowParams
                .newBuilder()
                .setSkuDetails(skuDetailsList.get(0))
                .build();


        billingClient.launchBillingFlow(this, billingFlowParams);


    }

    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<com.android.billingclient.api.Purchase> list) {

        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
            handlePurchase(list);


        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
            com.android.billingclient.api.Purchase.PurchasesResult purchaseResult = billingClient.queryPurchases(BillingClient.SkuType.SUBS);
            if (purchaseResult.getPurchasesList() != null && purchaseResult.getPurchasesList().size() > 0) {
                handlePurchase(purchaseResult.getPurchasesList());
            } else {
                finish();
            }
        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.ERROR) {

            return;
        } else {


        }
    }

    private void handlePurchase(List<com.android.billingclient.api.Purchase> purchases) {


        if (purchases != null && purchases.size() > 0) {

            if (!purchases.get(0).isAcknowledged()) {

                AcknowledgePurchaseParams acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchases.get(0).getPurchaseToken()).build();

                billingClient.acknowledgePurchase(acknowledgePurchaseParams, new AcknowledgePurchaseResponseListener() {
                    @Override
                    public void onAcknowledgePurchaseResponse(@NonNull BillingResult billingResult) {
                        //Log.e("handle","handle----"+billingResult.getDebugMessage()+"<<code>>"+billingResult.getResponseCode()
                        //+"<<purchase>>"+purchases.get(0).getSku()+"<<isAck>>"+purchases.get(0).isAcknowledged()
                        //+"<<purchasedTime>>"+purchases.get(0).getPurchaseTime()+"<<state>>"+purchases.get(0).getPurchaseState());
                        if (billingResult != null && billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {

                        } else {
                            //setUserDetails(purchases.get(0).getPurchaseToken(), skuList.get(0));
                        }

                    }
                });

            } else {
            }


            if (purchases != null && purchases.get(0).getPurchaseState() == 1) {
                //TODO: ENABLE YOUR PRO FEATURES!!
                isSubValid = true;

                if (Constants.isNetworkAvailable(context)) {
                   /* if (prefsManager.getKeySubId().length() > 0) {
                        if (emailId != null) {
                            Log.e("new_setUserDetails_from", "_newone");
                            setUserDetails(prefsManager.getKeySubId(), purchases.get(0).getSku());
                        } else {
                            Toast.makeText(mcontext, "plaese select emial id", Toast.LENGTH_SHORT).show();
                        }

                    } else {

                    }*/
                } else {


                }
            } else {

            }


        } else {

            allowBackpress = true;
        }


    }

    private void checkSubscription() {

        Log.e("checkSubcription", "checkSubcription");
        billingClient = BillingClient
                .newBuilder(this)
                .setListener(this)
                .enablePendingPurchases()
                .build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {

                Log.e("billingResult", String.valueOf(billingResult.getResponseCode()));

                if (billingResult != null && billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {

                    Log.e("STATUS", "BillingResponseCode_OK");

                    com.android.billingclient.api.Purchase.PurchasesResult purchaseResult = billingClient.queryPurchases(BillingClient.SkuType.SUBS);


                    if (purchaseResult.getPurchasesList() != null && purchaseResult.getPurchasesList().size() > 0) {

                        if (purchaseResult.getPurchasesList().get(0).getPackageName().equalsIgnoreCase("com.mylakemaps")) {
                            Log.e("package_name", "matched");
                            //for restore purpose
                            handlePurchase(purchaseResult.getPurchasesList());
                        }

                    } else {
                        Log.e("STATUS", "NO_PREVIOUS_PURCHASE");

                    }
                } else {
                    Log.e("STATUS", "onBillingSetupFailed");
                }
            }

            @Override
            public void onBillingServiceDisconnected() {

            }
        });

    }

    @Override
    public void onBackPressed() {
        if (allowBackpress)
            super.onBackPressed();
    }
}