package com.impinge.soul.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.impinge.soul.R;
import com.impinge.soul.activities.NotificationActivity;
import com.impinge.soul.adapter.CategoryAdapter;
import com.impinge.soul.adapter.ExploreAdapter;
import com.impinge.soul.adapter.SearchListAdapter;
import com.impinge.soul.adapter.VideoAdapter;
import com.impinge.soul.models.CategoryModel;
import com.impinge.soul.models.CategoryPojoModel;
import com.impinge.soul.models.ExplorePojo;
import com.impinge.soul.models.ExploreVideos;
import com.impinge.soul.models.SearchPojo;
import com.impinge.soul.models.SearchSongModel;
import com.impinge.soul.models.songsPojo;
import com.impinge.soul.retrofit.APIClient;
import com.impinge.soul.retrofit.APIInterface;
import com.impinge.soul.retrofit.response.RestResponse;
import com.impinge.soul.util.Constants;
import com.impinge.soul.util.Utility;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment implements View.OnClickListener {

    private RecyclerView category_recylerview, explore_recylerview, video_recylerview, search_recycler;
    CategoryAdapter categoryAdapter;
    ExploreAdapter exploreAdapter;
    VideoAdapter videoAdapter;
    NestedScrollView nested_scroll;
    private ImageView notification;
    private EditText et_search;
    RelativeLayout layer_dummy;
    SearchListAdapter searchListAdapter;
    ArrayList<SearchSongModel> searchList = new ArrayList<>();
    LinearLayout main_lay, no_item_lay;
    ArrayList<songsPojo> explore_songs_list = new ArrayList<>();
    ArrayList<ExploreVideos> explore_video_list = new ArrayList<>();
    ArrayList<CategoryModel> search_category_list = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search, container, false);
        findId(view);

        callSearchCatApi();
        return view;
    }

    private void callSearchCatApi() {
        try {
            Utility utility = Utility.getInstance(getActivity());

            utility.showLoading(getString(R.string.please_wait));
            APIInterface apiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);
            Call<RestResponse<CategoryPojoModel>> callApi = apiInterface.searchCategory();
            callApi.enqueue(new Callback<RestResponse<CategoryPojoModel>>() {
                @Override
                public void onResponse(Call<RestResponse<CategoryPojoModel>> call, Response<RestResponse<CategoryPojoModel>> response) {
                    utility.hideLoading();

                    if (response.code() == 200) {
                        search_category_list.addAll(response.body().getData().getCategory());

                        if (search_category_list.size() > 0) {
                            setCategoryAdapter(search_category_list);
                        }

                        callExploreApi();

                    } else {

                    }
                }

                @Override
                public void onFailure(Call<RestResponse<CategoryPojoModel>> call, Throwable t) {
                    Log.e("exception_msg", t.getMessage());
                    utility.hideLoading();

                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void setCategoryAdapter(ArrayList<CategoryModel> search_category_list) {

        categoryAdapter = new CategoryAdapter(getActivity(), search_category_list);
        category_recylerview.setAdapter(categoryAdapter);

    }

    private void callExploreApi() {

        try {
            Utility utility = Utility.getInstance(getActivity());

            utility.showLoading(getString(R.string.please_wait));
            APIInterface apiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);
            Call<RestResponse<ExplorePojo>> callApi = apiInterface.exploreSongs();
            callApi.enqueue(new Callback<RestResponse<ExplorePojo>>() {
                @Override
                public void onResponse(Call<RestResponse<ExplorePojo>> call, Response<RestResponse<ExplorePojo>> response) {
                    utility.hideLoading();

                    Log.e("response_msg", String.valueOf(response.code()));
                    if (response.code() == 200) {
                        explore_songs_list.addAll(response.body().getData().getEffect_data().getSongs());
                        explore_video_list.addAll(response.body().getData().getEffect_data().getVideos());

                        Log.e("explore_list_song", String.valueOf(explore_video_list.size()));


                        if (explore_songs_list.size() > 0) {
                            setExploreAdapter(explore_songs_list);

                        } else {
                        }

                        if (explore_video_list.size() > 0) {
                            setVideoAdapter(explore_video_list);
                        } else {
                        }

                    } else {

                    }
                }

                @Override
                public void onFailure(Call<RestResponse<ExplorePojo>> call, Throwable t) {
                    Log.e("exception_msg", t.getMessage());
                    utility.hideLoading();

                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void setVideoAdapter(ArrayList<ExploreVideos> explore_video_list) {
        videoAdapter = new VideoAdapter(getActivity(), explore_video_list);
        video_recylerview.setAdapter(videoAdapter);
    }

    private void setExploreAdapter(ArrayList<songsPojo> explore_list) {
        exploreAdapter = new ExploreAdapter(getActivity(), explore_list);
        explore_recylerview.setAdapter(exploreAdapter);
    }

    private void showSearchAdapter(ArrayList<SearchSongModel> searchList) {
        searchListAdapter = new SearchListAdapter(getActivity(), searchList);
        search_recycler.setAdapter(searchListAdapter);
    }


    private void findId(View view) {
        nested_scroll = view.findViewById(R.id.nested_scroll);
        nested_scroll.setNestedScrollingEnabled(false);

        search_recycler = view.findViewById(R.id.search_recycler);
        category_recylerview = view.findViewById(R.id.category_recylerview);
        explore_recylerview = view.findViewById(R.id.explore_recylerview);
        video_recylerview = view.findViewById(R.id.video_recylerview);
        main_lay = view.findViewById(R.id.main_lay);
        no_item_lay = view.findViewById(R.id.no_item_lay);

        et_search = view.findViewById(R.id.et_search);
        layer_dummy = view.findViewById(R.id.layer_dummy);
        notification = view.findViewById(R.id.notification);
        notification.setOnClickListener(this);

        search_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        search_recycler.setHasFixedSize(true);

        category_recylerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        category_recylerview.setHasFixedSize(false);
        category_recylerview.setNestedScrollingEnabled(false);


        explore_recylerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        explore_recylerview.setHasFixedSize(false);
        explore_recylerview.setNestedScrollingEnabled(false);

        video_recylerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        video_recylerview.setHasFixedSize(false);
        video_recylerview.setNestedScrollingEnabled(false);

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 0) {
                    main_lay.setVisibility(View.GONE);
                    layer_dummy.setVisibility(View.VISIBLE);

                } else if (s.length() == 0) {
                    main_lay.setVisibility(View.VISIBLE);
                    layer_dummy.setVisibility(View.GONE);
                    no_item_lay.setVisibility(View.GONE);
                }

                if (s.length() > 2) {
                    if (Constants.isNetworkAvailable(getActivity())) {
                        callSearchApi(s);
                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.check_network_connection), Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

    }


    private void callSearchApi(CharSequence s) {

        try {
            Utility utility = Utility.getInstance(getActivity());

            //   utility.showLoading(getString(R.string.please_wait));
            APIInterface apiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);
            Call<RestResponse<SearchPojo>> callApi = apiInterface.search(String.valueOf(s));
            callApi.enqueue(new Callback<RestResponse<SearchPojo>>() {
                @Override
                public void onResponse(Call<RestResponse<SearchPojo>> call, Response<RestResponse<SearchPojo>> response) {
                    // utility.hideLoading();

                    Log.e("response_msg", String.valueOf(response.code()));
                    if (response.code() == 200) {
                        Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();


                        searchList.clear();
                        searchList.addAll(response.body().getData().getSongs());

                        if (searchList.size() > 0) {
                            layer_dummy.setVisibility(View.VISIBLE);
                            nested_scroll.setVisibility(View.VISIBLE);
                            no_item_lay.setVisibility(View.GONE);
                            showSearchAdapter(searchList);

                        } else {
                            nested_scroll.setVisibility(View.GONE);
                            no_item_lay.setVisibility(View.VISIBLE);
                            layer_dummy.setVisibility(View.GONE);
                        }

                        Log.e("sizeeeeeeeeee", String.valueOf(searchList.size()));

                    } else {
                        layer_dummy.setVisibility(View.GONE);

                        Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<RestResponse<SearchPojo>> call, Throwable t) {
                    Log.e("exception_msg", t.getMessage());
                    // utility.hideLoading();

                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.notification:
                startActivity(new Intent(getActivity(), NotificationActivity.class));
                getActivity().overridePendingTransition(R.anim.trans_slide_inleft, R.anim.trans_slide_out_left);

                break;
        }
    }
}
