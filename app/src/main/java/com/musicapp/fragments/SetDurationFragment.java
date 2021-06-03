package com.musicapp.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.ikovac.timepickerwithseconds.TimePicker;
import com.musicapp.R;
import com.musicapp.activities.StartDurationActivity;
import com.musicapp.adapter.AlarmSoundAdapter;
import com.musicapp.models.CategoryModel;
import com.musicapp.models.CategoryPojoModel;
import com.musicapp.models.User;
import com.musicapp.retrofit.APIClient;
import com.musicapp.retrofit.APIInterface;
import com.musicapp.retrofit.response.RestResponse;
import com.musicapp.util.Utility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import io.github.deweyreed.scrollhmspicker.ScrollHmsPicker;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetDurationFragment extends Fragment implements View.OnClickListener {
    Context context;
    ScrollHmsPicker start_timer;
    ArrayList<CategoryModel> spinner_list = new ArrayList<>();
    SmartMaterialSpinner nice_spinner;
    int Selected_id;
    TextView select_time, set_time, set;
    private Calendar calendar;
    private int mYear, mMonth, mDay, mHour, mMinute;
    DatePickerDialog datePickerDialog;
    String selected_date;
    TimePicker time_picker;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.set_duration, container, false);
        context = container.getContext();
        findId(view);
        getAllCategories(view);
        calendar = Calendar.getInstance();
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        return view;
    }

    private void findId(View view) {
        nice_spinner = view.findViewById(R.id.nice_spinner);
        time_picker = view.findViewById(R.id.time_picker);
        time_picker.setIs24HourView(true);
        set_time = view.findViewById(R.id.set_time);
        select_time = view.findViewById(R.id.select_time);
        set = view.findViewById(R.id.set);
        select_time.setOnClickListener(this);
        set.setOnClickListener(this);
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
                        ArrayAdapter<CategoryModel> adapter = new ArrayAdapter<CategoryModel>(getActivity(), android.R.layout.simple_spinner_item, spinner_list);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        nice_spinner.setAdapter(adapter);
                        nice_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                CategoryModel user = (CategoryModel) parent.getSelectedItem();
                                String name = user.getName();
                                Selected_id = user.getId();
                                nice_spinner.setHint("");
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_time:
                Toast.makeText(context, "select_time", Toast.LENGTH_SHORT).show();

                datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                        // adding the selected date in the edittext
                        set_time.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                        selected_date = set_time.getText().toString();

                        openTimePicker();


                    }
                }, mYear, mMonth, mDay);

                // set maximum date to be selected as today
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

                // show the dialog
                datePickerDialog.show();
                break;


            case R.id.set:

                //callsetDurationApi();

                int hours = time_picker.getCurrentHour();
                int minutes = time_picker.getCurrentMinute();
                int seconds = time_picker.getCurrentSeconds();

                Log.e("TIMEEEEE", "---" + hours + "---" + minutes + "---" + seconds);
                getActivity().startActivity(new Intent(getActivity(), StartDurationActivity.class));

                break;
        }
    }

    private void openTimePicker() {

        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {

                        Calendar datetime = Calendar.getInstance();
                        Calendar c = Calendar.getInstance();
                        datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        datetime.set(Calendar.MINUTE, minute);
                        if (datetime.getTimeInMillis() >= c.getTimeInMillis()) {
                            //it's after current
                            int hour = hourOfDay % 12;
                            set_time.setText(selected_date + " " + String.format("%02d:%02d %s", hour == 0 ? 12 : hour,
                                    minute, hourOfDay < 12 ? "am" : "pm"));


                        } else {
                            //it's before current'
                            Toast.makeText(getActivity(), "Please select valid Time", Toast.LENGTH_LONG).show();
                        }

                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }


    private void callsetDurationApi() {
        Utility utility = Utility.getInstance(getActivity());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("user", "");
        hashMap.put("category_id", "");
        hashMap.put("current_time", "");
        hashMap.put("duration_in_min", "");

        Log.e("setDurationData", String.valueOf(hashMap));

        utility.showLoading(getString(R.string.please_wait));
        APIInterface apiInterface = APIClient.getClient(context).create(APIInterface.class);
        Call<RestResponse<User>> callApi = apiInterface.setDuration(hashMap);
        callApi.enqueue(new Callback<RestResponse<User>>() {
            @Override
            public void onResponse(Call<RestResponse<User>> call, Response<RestResponse<User>> response) {


                Log.e("response_msg", String.valueOf(response.code()));
                if (response.code() == 200) {

                    User user = response.body().data();

                    utility.hideLoading();
                    getActivity().startActivity(new Intent(getActivity(), StartDurationActivity.class));

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


}
