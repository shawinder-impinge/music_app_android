package com.musicapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.musicapp.R;
import com.musicapp.models.User;
import com.musicapp.retrofit.APIClient;
import com.musicapp.retrofit.APIInterface;
import com.musicapp.retrofit.response.RestResponse;
import com.musicapp.util.Constants;
import com.musicapp.util.PreferenceData;
import com.musicapp.util.SharedPreference;
import com.musicapp.util.Utility;
import com.musicapp.util.sweetdialog.Alert;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private static final int RC_SIGN_IN = 9022;
    Context context = this;
    private TextView login, dont_have_account, forgot_pass;
    VideoView videoview;
    private EditText et_email, et_password;
    private GoogleApiClient mGoogleSignInClient;
    private String serverClientId = "1075297276319-8t0pbneb83k12bc19i0lda5a16ev1abr.apps.googleusercontent.com";
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private String fcmToken, video_link = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fullWindow();
        findId();

        if (getIntent().hasExtra("link")) {
            video_link = getIntent().getStringExtra("link");
        }

        integrateGoogleSignIN();
        integrateFacebookSignIN();
        playVideoBackground();
        getFcmToken();
    }

    private static final String EMAIL = "email";

    private void integrateFacebookSignIN() {

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        if (isLoggedIn) {
            LoginManager
                    .getInstance().logOut();
        }

        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("data4525", loginResult.getAccessToken().getToken());
                GraphRequestBatch batch = new GraphRequestBatch(
                        GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject jsonObject,
                                            GraphResponse response) {
                                        Log.d("data4525", jsonObject.toString());
                                        callFacebookApi(loginResult.getAccessToken().getToken());
                                        // Application code for user
                                    }
                                }),
                        GraphRequest.newMyFriendsRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONArrayCallback() {
                            @Override
                            public void onCompleted(
                                    JSONArray jsonArray,
                                    GraphResponse response) {
                                // Application code for users friends
                            }
                        })
                );
                batch.addCallback(new GraphRequestBatch.Callback() {
                    @Override
                    public void onBatchCompleted(GraphRequestBatch graphRequests) {
                        // Application code for when the batch finishes
                    }
                });
                batch.executeAsync();

                // App code
            }

            @Override
            public void onCancel() {
                Log.d("data4rt4df", String.valueOf("gg"));

                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d("data4rt4", String.valueOf(exception.toString()));

                // App code
            }
        });
    }

    private void getFcmToken() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(LoginActivity.this,
                instanceIdResult -> {
                    fcmToken = instanceIdResult.getToken();
                    Log.e("newToken", fcmToken);
                });

    }

    private void integrateGoogleSignIN() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
                .requestServerAuthCode(serverClientId)
                .requestEmail()
                .build();
        mGoogleSignInClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(LoginActivity.this /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }


    private void findId() {
        login = findViewById(R.id.login);
        forgot_pass = findViewById(R.id.forgot_pass);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        dont_have_account = findViewById(R.id.dont_have_account);
        login.setOnClickListener(this);
        dont_have_account.setOnClickListener(this);
        forgot_pass.setOnClickListener(this);
        dont_have_account.setText(Html.fromHtml("<u>Sign Up"));

    }

    private void playVideoBackground() {
        videoview = (VideoView) findViewById(R.id.videoView);
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.app_video);
        videoview.setVideoURI(uri);
        videoview.start();

        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoview.start();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:

                if (isValidate()) {

                    if (Constants.isNetworkAvailable(context)) {
                        callLoginApi();
                    } else {
                        Alert.snackBar(v, getResources().getString(R.string.check_network_connection));
                    }
                }


                break;

            case R.id.facebook:


                if (Constants.isNetworkAvailable(context)) {
                    loginButton.performClick();
                } else {
                    Alert.snackBar(v, getResources().getString(R.string.check_network_connection));
                }


                break;

            case R.id.gmail:


                if (Constants.isNetworkAvailable(context)) {
                    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleSignInClient);
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                } else {
                    Alert.snackBar(v, getResources().getString(R.string.check_network_connection));
                }


                break;

            case R.id.dont_have_account:
                redirectToSignUp();
                break;
            case R.id.forgot_pass:
                startActivity(new Intent(LoginActivity.this, ForgotPassword.class));
                overridePendingTransition(R.anim.trans_slide_inleft, R.anim.trans_slide_out_left);

                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);

        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);


            callgmailLoginApi(account);
            // Signed in successfully, show authenticated UI.
            //  updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            //  Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            //  updateUI(null);
        }
    }

    private void redirectToPersonalizedScreen() {
        Intent i = new Intent(LoginActivity.this, PersonalizeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        overridePendingTransition(R.anim.trans_slide_inleft, R.anim.trans_slide_out_left);
    }

    private void callLoginApi() {

        Utility utility = Utility.getInstance(this);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("email", et_email.getText().toString());
        hashMap.put("password", et_password.getText().toString());
        hashMap.put("device_id", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        hashMap.put("device_token", fcmToken);
        hashMap.put("device_type", "android");

        Log.e("LOGIN_DATA", String.valueOf(hashMap));
        utility.showLoading(getString(R.string.please_wait));
        APIInterface apiInterface = APIClient.getClient(context).create(APIInterface.class);
        Call<RestResponse<User>> callApi = apiInterface.login(hashMap);
        callApi.enqueue(new Callback<RestResponse<User>>() {
            @Override
            public void onResponse(Call<RestResponse<User>> call, Response<RestResponse<User>> response) {


                Log.e("response_msg", String.valueOf(response.code()));
                if (response.code() == 200) {

                    User user = response.body().data();

                    utility.hideLoading();
                    SharedPreference.savePreferenceData(LoginActivity.this, PreferenceData.USER_ID, response.body().getId());
                    SharedPreference.savePreferenceData(LoginActivity.this, PreferenceData.TOKEN, user.token);
                    SharedPreference.savePreferenceData(LoginActivity.this, PreferenceData.USERNAME, response.body().getUsername());
                    SharedPreference.savePreferenceData(LoginActivity.this, PreferenceData.LOGIN_TYPE_USED, PreferenceData.LOGIN_TYPE.STANDARD.name());

                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                    redirectToPersonalizedScreen();
                } else {
                    utility.hideLoading();
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RestResponse<User>> call, Throwable t) {
                Log.e("exception_msg", t.getMessage());
                utility.hideLoading();

            }
        });
    }

    private void callFacebookApi(String token) {

        Utility utility = Utility.getInstance(this);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("token", token);
        hashMap.put("device_id", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        hashMap.put("device_token", fcmToken);
        hashMap.put("device_type", "android");

        Log.e("LOGIN_DATA", String.valueOf(hashMap));
        utility.showLoading(getString(R.string.please_wait));
        APIInterface apiInterface = APIClient.getClient(context).create(APIInterface.class);
        Call<RestResponse<User>> callApi = apiInterface.facebooklogin(hashMap);
        callApi.enqueue(new Callback<RestResponse<User>>() {
            @Override
            public void onResponse(Call<RestResponse<User>> call, Response<RestResponse<User>> response) {


                Log.e("response_msg", String.valueOf(response.code()));
                if (response.code() == 200) {

                    User user = response.body().data();

                    utility.hideLoading();
                    SharedPreference.savePreferenceData(LoginActivity.this, PreferenceData.USER_ID, response.body().getId());
                    SharedPreference.savePreferenceData(LoginActivity.this, PreferenceData.TOKEN, user.token);
                    SharedPreference.savePreferenceData(LoginActivity.this, PreferenceData.USERNAME, response.body().getUsername());
                    SharedPreference.savePreferenceData(LoginActivity.this, PreferenceData.LOGIN_TYPE_USED, PreferenceData.LOGIN_TYPE.FACEBOOK.name());

                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                    redirectToPersonalizedScreen();
                } else {
                    utility.hideLoading();
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RestResponse<User>> call, Throwable t) {
                Log.e("exception_msg", t.getMessage());
                utility.hideLoading();

            }
        });
    }

    private void callgmailLoginApi(GoogleSignInAccount account) {

        Utility utility = Utility.getInstance(this);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("email", account.getEmail());
        hashMap.put("username", account.getDisplayName());
        hashMap.put("name", account.getGivenName());
        hashMap.put("device_id", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        hashMap.put("device_token", fcmToken);
        hashMap.put("device_type", "android");

        Log.e("LOGIN_DATA", String.valueOf(hashMap));
        utility.showLoading(getString(R.string.please_wait));
        APIInterface apiInterface = APIClient.getClient(context).create(APIInterface.class);
        Call<RestResponse<User>> callApi = apiInterface.googleLogin(hashMap);
        callApi.enqueue(new Callback<RestResponse<User>>() {
            @Override
            public void onResponse(Call<RestResponse<User>> call, Response<RestResponse<User>> response) {


                Log.e("response_msg", String.valueOf(response.code()));
                if (response.code() == 200) {

                    User user = response.body().data();

                    utility.hideLoading();
                    SharedPreference.savePreferenceData(LoginActivity.this, PreferenceData.USER_ID, response.body().getId());
                    SharedPreference.savePreferenceData(LoginActivity.this, PreferenceData.TOKEN, user.token);
                    SharedPreference.savePreferenceData(LoginActivity.this, PreferenceData.USERNAME, response.body().getUsername());
                    SharedPreference.savePreferenceData(LoginActivity.this, PreferenceData.LOGIN_TYPE_USED, PreferenceData.LOGIN_TYPE.GMAIL.name());

                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                    redirectToPersonalizedScreen();
                } else {
                    utility.hideLoading();
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RestResponse<User>> call, Throwable t) {
                Log.e("exception_msg", t.getMessage());
                utility.hideLoading();

            }
        });
    }

    private void redirectToSignUp() {
        startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
        overridePendingTransition(R.anim.trans_slide_inleft, R.anim.trans_slide_out_left);


    }

    @Override
    protected void onResume() {
        super.onResume();
        playVideoBackground();

    }

    private boolean isValidate() {

        if (et_email.getText().toString().trim().isEmpty()) {

            Alert.showWarningAlert(context, "Please enter email");

            return false;

        } else if (!et_email.getText().toString().trim().matches(Patterns.EMAIL_ADDRESS.pattern())) {


            Alert.showWarningAlert(context, "Please enter valid email");


            return false;

        } else if (et_password.getText().toString().trim().isEmpty()) {


            Alert.showWarningAlert(context, "Please enter password");


            return false;

        }

        return true;
    }

}