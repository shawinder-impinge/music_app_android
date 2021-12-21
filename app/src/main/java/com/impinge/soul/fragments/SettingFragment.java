package com.impinge.soul.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.impinge.soul.R;
import com.impinge.soul.activities.ChangePasswordActivity;
import com.impinge.soul.activities.ContactUsActivity;
import com.impinge.soul.activities.DownloadActivity;
import com.impinge.soul.activities.FavouriteListActivity;
import com.impinge.soul.activities.LoginActivity;
import com.impinge.soul.activities.PrivacyPolicyActivity;
import com.impinge.soul.activities.ProfileActivity;
import com.impinge.soul.models.User;
import com.impinge.soul.retrofit.APIClient;
import com.impinge.soul.retrofit.APIInterface;
import com.impinge.soul.retrofit.response.RestResponse;
import com.impinge.soul.util.Constants;
import com.impinge.soul.util.PreferenceData;
import com.impinge.soul.util.SharedPreference;
import com.impinge.soul.util.Utility;
import com.impinge.soul.util.sweetdialog.Alert;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingFragment extends Fragment implements View.OnClickListener {
    Context context;
    private TextView title, downloads, favourites, change_password, profile, manage_subscription, privacy_policy, contact_us, logout;
    private ImageView back;
    private GoogleApiClient mGoogleSignInClient;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting, container, false);
        context = container.getContext();
        findId(view);
        return view;
    }

    private void findId(View view) {
        title = view.findViewById(R.id.title);
        downloads = view.findViewById(R.id.downloads);
        change_password = view.findViewById(R.id.change_password);
        profile = view.findViewById(R.id.profile);
        manage_subscription = view.findViewById(R.id.manage_subscription);
        privacy_policy = view.findViewById(R.id.privacy_policy);
        contact_us = view.findViewById(R.id.contact_us);
        logout = view.findViewById(R.id.logout);
        favourites = view.findViewById(R.id.favourites);
        title.setText("Setting & Info");
        back = view.findViewById(R.id.back);
        back.setVisibility(View.GONE);
        favourites.setOnClickListener(this);
        downloads.setOnClickListener(this);
        change_password.setOnClickListener(this);
        profile.setOnClickListener(this);
        manage_subscription.setOnClickListener(this);
        privacy_policy.setOnClickListener(this);
        contact_us.setOnClickListener(this);
        logout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.downloads:
                startActivity(new Intent(getActivity(), DownloadActivity.class));
                getActivity().overridePendingTransition(R.anim.trans_slide_inleft, R.anim.trans_slide_out_left);
                break;

            case R.id.favourites:
                startActivity(new Intent(getActivity(), FavouriteListActivity.class));
                getActivity().overridePendingTransition(R.anim.trans_slide_inleft, R.anim.trans_slide_out_left);
                break;

            case R.id.change_password:
                startActivity(new Intent(getActivity(), ChangePasswordActivity.class));
                getActivity().overridePendingTransition(R.anim.trans_slide_inleft, R.anim.trans_slide_out_left);
                break;

            case R.id.profile:
                startActivity(new Intent(getActivity(), ProfileActivity.class));
                getActivity().overridePendingTransition(R.anim.trans_slide_inleft, R.anim.trans_slide_out_left);
                break;

            case R.id.manage_subscription:

                break;

            case R.id.privacy_policy:
                startActivity(new Intent(getActivity(), PrivacyPolicyActivity.class));
                getActivity().overridePendingTransition(R.anim.trans_slide_inleft, R.anim.trans_slide_out_left);
                break;

            case R.id.contact_us:
                startActivity(new Intent(getActivity(), ContactUsActivity.class));
                getActivity().overridePendingTransition(R.anim.trans_slide_inleft, R.anim.trans_slide_out_left);
                break;
            case R.id.logout:
                if (Constants.isNetworkAvailable(context)) {

                    if (SharedPreference.fetchPrefenceData(getActivity(), PreferenceData.LOGIN_TYPE_USED) == PreferenceData.LOGIN_TYPE.FACEBOOK.name()) {
                        AccessToken accessToken = AccessToken.getCurrentAccessToken();
                        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

                        if (isLoggedIn) {
                            LoginManager
                                    .getInstance().logOut();
                        }
                    } else if (SharedPreference.fetchPrefenceData(getActivity(), PreferenceData.LOGIN_TYPE_USED) == PreferenceData.LOGIN_TYPE.GMAIL.name()) {

// integrateGoogleSignIN();
// Auth.GoogleSignInApi.signOut(mGoogleSignInClient).setResultCallback(
//                                new ResultCallback<Status>() {
//                                    @Override
//                                    public void onResult(Status status) {
//                                        Toast.makeText(getActivity(),status.getStatusMessage(),Toast.LENGTH_SHORT).show();
//
//                                    }
//                                });
                    }
                    callLogout();
                } else {
                    Alert.snackBar(v, getResources().getString(R.string.check_network_connection));
                }
                break;
        }
    }

    private String serverClientId = "1075297276319-8t0pbneb83k12bc19i0lda5a16ev1abr.apps.googleusercontent.com";

    private void integrateGoogleSignIN() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
                .requestServerAuthCode(serverClientId)
                .requestEmail()
                .build();
        mGoogleSignInClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity() /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    private void callLogout() {

        try {
            Utility utility = Utility.getInstance(context);
            utility.showLoading(getString(R.string.please_wait));
            APIInterface apiInterface = APIClient.getClient(context).create(APIInterface.class);
            Call<RestResponse<User>> callApi = apiInterface.logout();
            callApi.enqueue(new Callback<RestResponse<User>>() {
                @Override
                public void onResponse(Call<RestResponse<User>> call, Response<RestResponse<User>> response) {
                    Log.e("response_code", String.valueOf(response.code()));

                    if (response.code() == 200) {
                        utility.hideLoading();
                        SharedPreference.savePreferenceData(context, PreferenceData.TOKEN, "");
                        Toast.makeText(context, "Logout successfully", Toast.LENGTH_SHORT).show();
                        backToLogin();

                    } else {
                        utility.hideLoading();
                    }

                }

                @Override
                public void onFailure(Call<RestResponse<User>> call, Throwable t) {
                    Log.e("exception_msg", t.getMessage());
                    utility.hideLoading();
                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void backToLogin() {
        Intent i = new Intent(getActivity(), LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        getActivity().overridePendingTransition(R.anim.trans_slide_inleft, R.anim.trans_slide_out_left);
    }
}
