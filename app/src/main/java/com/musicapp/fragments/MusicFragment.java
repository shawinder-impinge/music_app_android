package com.musicapp.fragments;

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

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.musicapp.R;
import com.musicapp.activities.AlbumActivity;
import com.musicapp.activities.AudioPlayerViewActivity;
import com.musicapp.activities.LoginActivity;
import com.musicapp.activities.SignUpActivity;
import com.musicapp.activities.ViewAllActivity;
import com.musicapp.adapter.ExploreAdapter;
import com.musicapp.adapter.MusicCategoryAdapter;
import com.musicapp.adapter.SongListAdpter;
import com.musicapp.models.CategoryModel;
import com.musicapp.models.CategoryPojo;
import com.musicapp.models.CategoryPojoModel;
import com.musicapp.models.DataPojo;
import com.musicapp.models.User;
import com.musicapp.models.songsPojo;
import com.musicapp.retrofit.APIClient;
import com.musicapp.retrofit.APIInterface;
import com.musicapp.retrofit.response.RestResponse;
import com.musicapp.util.Utility;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MusicFragment extends Fragment implements View.OnClickListener {
    private TextView title;
    Context context;
    private ImageView back;
    RecyclerView category_recylerview;
    MusicCategoryAdapter musicCategoryAdapter;
    ArrayList<CategoryModel> category_list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.music, container, false);
        context = container.getContext();
        findId(view);
        getAllCategories();
        return view;
    }

    private void getAllCategories() {
        try {
            Utility utility = Utility.getInstance(getActivity());
            utility.showLoading(getString(R.string.please_wait));
            APIInterface apiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);
            Call<RestResponse<CategoryPojoModel>> callApi = apiInterface.getCategories();
            callApi.enqueue(new Callback<RestResponse<CategoryPojoModel>>() {
                @Override
                public void onResponse(Call<RestResponse<CategoryPojoModel>> call, Response<RestResponse<CategoryPojoModel>> response) {
                    utility.hideLoading();

                    Log.e("response_msg", String.valueOf(response.code()));
                    if (response.code() == 200) {
                        category_list.addAll(response.body().getData().getCategory());

                        setAdapter(category_list);

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

    private void findId(View view) {
        title = view.findViewById(R.id.title);
        back = view.findViewById(R.id.back);
        back.setVisibility(View.GONE);
        title.setText("Music");
        category_recylerview = view.findViewById(R.id.category_recylerview);
        category_recylerview.setLayoutManager(new LinearLayoutManager(context));
        category_recylerview.setHasFixedSize(true);
    }


    private void setAdapter(ArrayList<CategoryModel> list) {
        musicCategoryAdapter = new MusicCategoryAdapter(context, list);
        category_recylerview.setAdapter(musicCategoryAdapter);
        musicCategoryAdapter.viewItemClick(new MusicCategoryAdapter.OnClick() {
            @Override
            public void onItemclick(String id, String pos, String title) {
                Intent intent = new Intent(getActivity(), ViewAllActivity.class);
                intent.putExtra("music", "");
                intent.putExtra("id", Integer.parseInt(id));
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.trans_slide_inleft, R.anim.trans_slide_out_left);

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}
