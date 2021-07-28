package com.musicapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.musicapp.R;

import com.musicapp.activities.ChangePasswordActivity;
import com.musicapp.activities.DashboardActivity;
import com.musicapp.activities.NotificationActivity;
import com.musicapp.activities.SignUpActivity;
import com.musicapp.activities.SplashActivity;
import com.musicapp.activities.SubscriptionActivity;
import com.musicapp.activities.ViewAllActivity;
import com.musicapp.activities.WelcomeScreen;
import com.musicapp.adapter.MeditationAdapter;
import com.musicapp.adapter.MostPopularAdapter;
import com.musicapp.models.CategoryModel;
import com.musicapp.models.CategoryPojoModel;
import com.musicapp.models.ChangePassModel;
import com.musicapp.models.DataPojo;
import com.musicapp.models.MeditationPojo;
import com.musicapp.models.MostPopularPojo;
import com.musicapp.models.TodaySpecialPojo;
import com.musicapp.models.User;
import com.musicapp.models.ViewAllPojo;
import com.musicapp.models.songsPojo;
import com.musicapp.retrofit.APIClient;
import com.musicapp.retrofit.APIInterface;
import com.musicapp.retrofit.response.RestResponse;
import com.musicapp.util.PreferenceData;
import com.musicapp.util.SharedPreference;
import com.musicapp.util.Utility;
import com.musicapp.util.sweetdialog.Alert;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ForYouFragment extends Fragment implements View.OnClickListener {
    private TextView upgrade_premium, relax_text, browse_text, category_text;
    RecyclerView popular_recylerview, meditation_recylerview;
    MostPopularAdapter mostPopularAdapter;
    MeditationAdapter meditationAdapter;
    private VideoView video_view;
    ArrayList<songsPojo> meditationPojoArrayList = new ArrayList<>();
    ArrayList<MostPopularPojo> mostPopularPojoArrayList = new ArrayList<>();
    private ImageView notification;
    TextView mTvUsername, mostpopular_viewall, meditation_viewall, song_name, song_count;
    ImageView mIvPlay;
    MediaPlayer mediaPlayer;
    int meditation_id, Selected_id;
    ArrayList<CategoryModel> spinner_list = new ArrayList<>();
    ArrayList<songsPojo> most_popular_list = new ArrayList<>();
    ArrayList<songsPojo> other_categoryList = new ArrayList<>();
    View view;
    SmartMaterialSpinner nice_spinner;
    boolean isFirstTime = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.for_you, container, false);
        findId(view);
        // setRecycler();
        getAllCategories(view);
        getMostPopular();
        return view;
    }


    private void getAllCategories(View view) {
        try {
            Utility utility = Utility.getInstance(getActivity());
            //utility.showLoading(getString(R.string.please_wait));
            APIInterface apiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);
            Call<RestResponse<CategoryPojoModel>> callApi = apiInterface.getCategories();
            callApi.enqueue(new Callback<RestResponse<CategoryPojoModel>>() {
                @Override
                public void onResponse(Call<RestResponse<CategoryPojoModel>> call, Response<RestResponse<CategoryPojoModel>> response) {

                    Log.e("response", String.valueOf(response.code()));
                    if (response.code() == 200) {
                        spinner_list.addAll(response.body().getData().getCategory());

                        Selected_id = 1;
                        callSpinnerApi(Selected_id);

                        ArrayAdapter<CategoryModel> adapter = new ArrayAdapter<CategoryModel>(getActivity(), android.R.layout.simple_spinner_item, spinner_list);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        nice_spinner.setAdapter(adapter);
                        nice_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                CategoryModel user = (CategoryModel) parent.getSelectedItem();
                                String name = user.getName();
                                Selected_id = user.getId();
                                category_text.setText(name);
                                nice_spinner.setHint("");
                                callSpinnerApi(Selected_id);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    } else {

                    }
                }

                @Override
                public void onFailure(Call<RestResponse<CategoryPojoModel>> call, Throwable t) {
                    Log.e("exception_msg", t.getMessage());

                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void callSpinnerApi(int id) {

        try {
            Utility utility = Utility.getInstance(getActivity());

            // utility.showLoading(getString(R.string.please_wait));
            APIInterface apiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);
            Call<RestResponse<DataPojo>> callApi = apiInterface.getSongList(id);
            callApi.enqueue(new Callback<RestResponse<DataPojo>>() {
                @Override
                public void onResponse(Call<RestResponse<DataPojo>> call, Response<RestResponse<DataPojo>> response) {
                    //  utility.hideLoading();

                    Log.e("response_msg", String.valueOf(response.code()));
                    if (response.code() == 200) {

                        DataPojo dataPojo = response.body().data();

                        if (dataPojo.getCategoryPojoArrayList() != null && dataPojo.getCategoryPojoArrayList().size() > 0) {
                            other_categoryList.clear();
                            other_categoryList.addAll(dataPojo.getCategoryPojoArrayList().get(0).getSongs());

                            Log.e("MEDITAION_LIST", String.valueOf(other_categoryList.size()));

                            // setAdapter(meditation_list);
                            setRecycler(other_categoryList);
                        } else {

                        }

                    } else {
                        Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<RestResponse<DataPojo>> call, Throwable t) {
                    Log.e("exception_msg", t.getMessage());
                    //  utility.hideLoading();

                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }


    private void playVideo(String videoUrl) {


        Uri video = Uri.parse(APIClient.MEDIA_URL + videoUrl);
        video_view.setVideoURI(video);
        video_view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                if (mediaPlayer == null) {
                    mediaPlayer = mp;
                }
                mIvPlay.setImageDrawable(getResources().getDrawable(R.drawable.pause));

                mp.setLooping(true);
                video_view.start();
            }
        });
        mIvPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer == null) return;
                if (mediaPlayer.isPlaying()) {

                    mIvPlay.setImageDrawable(getResources().getDrawable(R.drawable.play_round));
                    mediaPlayer.pause();
                } else {
                    mIvPlay.setImageDrawable(getResources().getDrawable(R.drawable.pause));

                    mediaPlayer.start();
                }
            }
        });
    }

    private void findId(View view) {
        video_view = view.findViewById(R.id.video_view);
        nice_spinner = view.findViewById(R.id.nice_spinner);
        mTvUsername = view.findViewById(R.id.username);
        mostpopular_viewall = view.findViewById(R.id.mostpopular_viewall);
        meditation_viewall = view.findViewById(R.id.meditation_viewall);
        song_name = view.findViewById(R.id.song_name);
        song_count = view.findViewById(R.id.song_count);

        mTvUsername.setText(getResources().getString(R.string.good_morning) + ", " + SharedPreference.fetchPrefenceData(getActivity(), PreferenceData.USERNAME));
        notification = view.findViewById(R.id.notification);
        relax_text = view.findViewById(R.id.relax_text);
        browse_text = view.findViewById(R.id.browse_text);
        category_text = view.findViewById(R.id.category_text);
        upgrade_premium = view.findViewById(R.id.upgrade_premium);
        popular_recylerview = view.findViewById(R.id.popular_recylerview);
        meditation_recylerview = view.findViewById(R.id.meditation_recylerview);
        mIvPlay = view.findViewById(R.id.play);
        popular_recylerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        popular_recylerview.setHasFixedSize(true);
        meditation_recylerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        meditation_recylerview.setHasFixedSize(true);
        upgrade_premium.setOnClickListener(this);
        notification.setOnClickListener(this);
        mostpopular_viewall.setOnClickListener(this);
        meditation_viewall.setOnClickListener(this);

        Spanned text = Html.fromHtml("Relax with our<br/> <i>Sleep</i><br/> music collection");
        relax_text.setText(text);
        Spanned text2 = Html.fromHtml("Browse our<br/> <i>Library</i><br/> of music & chants");
        browse_text.setText(text2);
    }

    private void setRecycler(ArrayList<songsPojo> other_categoryList) {

        if (meditationAdapter == null) {
            // meditationAdapter = new MeditationAdapter(getActivity(), meditationPojoArrayList);
            meditationAdapter = new MeditationAdapter(getActivity(), other_categoryList);
            meditation_recylerview.setAdapter(meditationAdapter);
        } else {
            meditationAdapter.notifyDataSetChanged();
        }


    }


    private void getMostPopular() {

        try {
            Utility utility = Utility.getInstance(getActivity());

            utility.showLoading(getString(R.string.please_wait));
            APIInterface apiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);
            Call<RestResponse<ViewAllPojo>> callApi = apiInterface.mostPopular();
            callApi.enqueue(new Callback<RestResponse<ViewAllPojo>>() {
                @Override
                public void onResponse(Call<RestResponse<ViewAllPojo>> call, Response<RestResponse<ViewAllPojo>> response) {

                    utility.hideLoading();

                    Log.e("response_code", String.valueOf(response.code()));
                    if (response.code() == 200) {
                        most_popular_list.clear();
                        most_popular_list.addAll(response.body().getData().getSongs());

                        if (mostPopularAdapter == null) {
                            mostPopularAdapter = new MostPopularAdapter(getActivity(), most_popular_list);
                            popular_recylerview.setAdapter(mostPopularAdapter);
                        } else {
                            mostPopularAdapter.notifyDataSetChanged();
                        }

                        fetchDashboardData();

                    } else {

                    }
                }

                @Override
                public void onFailure(Call<RestResponse<ViewAllPojo>> call, Throwable t) {
                    Log.e("exception_msg", t.getMessage());
                    utility.hideLoading();

                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upgrade_premium:
                startActivity(new Intent(getActivity(), SubscriptionActivity.class));
                getActivity().overridePendingTransition(R.anim.trans_slide_inleft, R.anim.trans_slide_out_left);
                break;
            case R.id.notification:
                startActivity(new Intent(getActivity(), NotificationActivity.class));
                getActivity().overridePendingTransition(R.anim.trans_slide_inleft, R.anim.trans_slide_out_left);
                break;
            case R.id.mostpopular_viewall:

                startActivity(new Intent(getActivity(), ViewAllActivity.class).putExtra("most_popular", ""));
                getActivity().overridePendingTransition(R.anim.trans_slide_inleft, R.anim.trans_slide_out_left);
                break;

            case R.id.meditation_viewall:
                Intent intent = new Intent(getActivity(), ViewAllActivity.class);
                intent.putExtra("meditation", "");
                intent.putExtra("meditation_id", Selected_id);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.trans_slide_inleft, R.anim.trans_slide_out_left);

                break;

        }
    }


    private void fetchDashboardData() {

        try {
            Utility utility = Utility.getInstance(getActivity());

            // utility.showLoading(getString(R.string.please_wait));
            APIInterface apiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);
            Call<RestResponse<DataPojo>> callApi = apiInterface.dashboardData();
            callApi.enqueue(new Callback<RestResponse<DataPojo>>() {
                @Override
                public void onResponse(Call<RestResponse<DataPojo>> call, Response<RestResponse<DataPojo>> response) {
                    //utility.hideLoading();

                    Log.e("response_msg", String.valueOf(response.code()));
                    if (response.code() == 200) {
                        Log.e("RESPONSE_CODE", String.valueOf(response.code()));
                        Log.e("RESPONSE_MESSAGE", String.valueOf(response.body().msg()));
                        DataPojo dataPojo = response.body().data();

                      //  meditationPojoArrayList.addAll(dataPojo.getMeditation().getSongs());
                        // mostPopularPojoArrayList.addAll(dataPojo.getMost_popular_songs());

                        meditation_id = dataPojo.getMeditation().getId();

                        // playVideo(dataPojo.today_special.getVideo().get(0).getFile());
                        playVideo(dataPojo.today_special.getVideo().getFile());
                        song_name.setText(dataPojo.today_special.getName() + "-");
                        song_count.setText(dataPojo.today_special.getListen_count() + " " + "times");

                        int video_id = dataPojo.today_special.getId();
                        // setRecycler();


                        videoCountApi(video_id);

                    } else {

                    }
                }

                @Override
                public void onFailure(Call<RestResponse<DataPojo>> call, Throwable t) {
                    Log.e("exception_msg", t.getMessage());
                    // utility.hideLoading();

                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void videoCountApi(int video_id) {
        {
            Utility utility = Utility.getInstance(getActivity());
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("id", String.valueOf(video_id));
            APIInterface apiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);


            Call<RestResponse<User>> callApi = apiInterface.updatevideocount(hashMap);
            callApi.enqueue(new Callback<RestResponse<User>>() {
                @Override
                public void onResponse(Call<RestResponse<User>> call, Response<RestResponse<User>> response) {

                    if (response.code() == 200) {


                    } else {

                        Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<RestResponse<User>> call, Throwable t) {
                    Log.e("exception_msg", t.getMessage());
                    //utility.hideLoading();

                }
            });
        }
    }


}
