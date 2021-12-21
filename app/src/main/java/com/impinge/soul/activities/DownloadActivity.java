package com.impinge.soul.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.impinge.soul.R;
import com.impinge.soul.adapter.DownloadSongsAdapter;
import com.impinge.soul.localdatabase.DatabaseClient;
import com.impinge.soul.localdatabase.DownloadedSongsModel;

import java.util.List;

public class DownloadActivity extends AppCompatActivity {
    private TextView title;
    Context context = this;
    RecyclerView recyclerView;
    DownloadSongsAdapter downloadSongsAdapter;
    LinearLayout no_download_lay;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        findId();
        fetchSongsFromLocal();
    }

    private void fetchSongsFromLocal() {

        class GetDownloadedSongs extends AsyncTask<Void, Void, List<DownloadedSongsModel>> {

            @Override
            protected List<DownloadedSongsModel> doInBackground(Void... voids) {
                List<DownloadedSongsModel> taskList = DatabaseClient
                        .getInstance(DownloadActivity.this)
                        .getAppDatabase()
                        .taskDao()
                        .getAll();
                return taskList;
            }

            @Override
            protected void onPostExecute(List<DownloadedSongsModel> tasks) {
                super.onPostExecute(tasks);

                Log.e("list_size", String.valueOf(tasks.size()));


                if (tasks.size() > 0) {
                    no_download_lay.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    setAdapter(tasks);

                } else {
                    no_download_lay.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }

            }
        }
        GetDownloadedSongs gt = new GetDownloadedSongs();
        gt.execute();
    }


    private void setAdapter(List<DownloadedSongsModel> tasks) {
        downloadSongsAdapter = new DownloadSongsAdapter(context, tasks);
        recyclerView.setAdapter(downloadSongsAdapter);
    }

    private void findId() {
        recyclerView = findViewById(R.id.recyclerView);
        back = findViewById(R.id.back);
        no_download_lay = findViewById(R.id.no_download_lay);
        title = findViewById(R.id.title);
        title.setText("Downloads");
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
    }
}