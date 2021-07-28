package com.musicapp.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.dialog.DoubleDateAndTimePickerDialog;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.ikovac.timepickerwithseconds.MyTimePickerDialog;
import com.ikovac.timepickerwithseconds.TimePicker;
import com.musicapp.R;
import com.musicapp.activities.AlarmActivity;
import com.musicapp.activities.FavouriteListActivity;
import com.musicapp.activities.LoginActivity;
import com.musicapp.activities.StartDurationActivity;
import com.musicapp.models.CategoryModel;
import com.musicapp.models.CategoryPojoModel;
import com.musicapp.models.TimeModal;
import com.musicapp.models.User;
import com.musicapp.retrofit.APIClient;
import com.musicapp.retrofit.APIInterface;
import com.musicapp.retrofit.response.RestResponse;
import com.musicapp.util.PreferenceData;
import com.musicapp.util.SharedPreference;
import com.musicapp.util.Utility;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import io.github.deweyreed.scrollhmspicker.ScrollHmsPicker;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EndTimeFragment extends Fragment implements View.OnClickListener {

    Context context;
    TimePicker timePicker;
    TextView set, time1, time2, date_1, date_2;
    RelativeLayout alarm_lay;
    ScrollHmsPicker start_timer, end_timer;
    ArrayList<CategoryModel> spinner_list = new ArrayList<>();
    SmartMaterialSpinner nice_spinner;
    int Selected_id;
    SingleDateAndTimePicker single_time_picker;
    private Calendar calendar;
    private int mYear, mMonth, mDay, mHour, mMinute;
    DatePickerDialog datePickerDialog;
    String selected_date, selected_date2,setTime,endTime,utcTime;
    String current_date = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fade_away_start, container, false);
        findId(view);
        getUtc();
        getAllCategories(view);
        calendar = Calendar.getInstance();
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);


        DateFormat df = DateFormat.getTimeInstance();
        df.setTimeZone(TimeZone.getTimeZone("gmt"));
        utcTime = df.format(new Date());


        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        current_date = day + "-" + (month + 1) + "-" + year;
        return view;
    }

    private void getUtc() {
        DateFormat df = DateFormat.getTimeInstance();
        //df.setTimeZone(TimeZone.getTimeZone("gmt"));
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        String gmtTime = df.format(new Date());
        Log.e("timezone_utc", gmtTime);
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

    private void findId(View view) {
        timePicker = view.findViewById(R.id.timePicker);
        nice_spinner = view.findViewById(R.id.nice_spinner);
        alarm_lay = view.findViewById(R.id.alarm_lay);
        set = view.findViewById(R.id.set);
        time1 = view.findViewById(R.id.time1);
        time2 = view.findViewById(R.id.time2);
        date_1 = view.findViewById(R.id.date_1);
        date_2 = view.findViewById(R.id.date_2);
        set.setOnClickListener(this);
        alarm_lay.setOnClickListener(this);
        time1.setOnClickListener(this);
        time2.setOnClickListener(this);

    }


    private void callAddAlarmApi() {
        Utility utility = Utility.getInstance(getActivity());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("user", SharedPreference.fetchPrefenceData(context, PreferenceData.USER_ID));
        hashMap.put("category_id", Selected_id);
        hashMap.put("set_time", selected_date);
        hashMap.put("end_time", selected_date2);

        Log.e("addAlarmData", String.valueOf(hashMap));

        utility.showLoading(getString(R.string.please_wait));
        APIInterface apiInterface = APIClient.getClient(context).create(APIInterface.class);
        Call<RestResponse<User>> callApi = apiInterface.addAlarm(hashMap);
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


    public boolean loadFragment(Fragment fragment, String start_hours, String start_minutes, String start_seconds) {
        //switching fragment
        if (fragment != null) {
            Bundle bundle = new Bundle();
            bundle.putString("hours", start_hours);
            bundle.putString("min", start_minutes);
            bundle.putString("second", start_seconds);
            fragment.setArguments(bundle);
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set:

               // getActivity().startActivity(new Intent(getActivity(), StartDurationActivity.class));


                callAddAlarmApi();
//                Calendar now = Calendar.getInstance();
//                String start_hours = String.valueOf(timePicker.getCurrentHour());
//                String start_minutes = String.valueOf(timePicker.getCurrentMinute());
//                String start_seconds = String.valueOf(timePicker.getCurrentSeconds());
                // loadFragment(new FadeAwayFragment(), start_hours, start_minutes, start_seconds);

                break;
            case R.id.alarm_lay:

                // loadFragment(new SetDurationFragment(), null, null, null);
                getActivity().startActivity(new Intent(getActivity(), AlarmActivity.class));

                break;
            case R.id.time1:

                datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                        // adding the selected date in the edittext
                        date_1.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                        selected_date = date_1.getText().toString();

                        Log.e("sellllllll", selected_date);
                        Log.e("currrrrrrr", selected_date);


                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            Date strDate = null;
                            strDate = sdf.parse(current_date);
                            if (new Date().after(strDate)) {
                                int catalog_outdated = 1;
                                Log.e("dateeeeeeeeeeeeee", "tomorrow");
                            } else {
                                int catalog_outdated = 2;

                                Log.e("dateeeeeeeeeeeeee", "today");
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                        openTimePicker();


                    }
                }, mYear, mMonth, mDay);

                // set maximum date to be selected as today
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

                // show the dialog
                datePickerDialog.show();

                break;
            case R.id.time2:

                datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                        // adding the selected date in the edittext
                        date_2.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                        selected_date2 = date_2.getText().toString();

                        openTimePicker2();


                    }
                }, mYear, mMonth, mDay);

                // set maximum date to be selected as today
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

                // show the dialog
                datePickerDialog.show();

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
                            date_1.setVisibility(View.VISIBLE);
                            //it's after current
                            int hour = hourOfDay % 12;
                            date_1.setText(selected_date + " " + String.format("%02d:%02d %s", hour == 0 ? 12 : hour,
                                    minute, hourOfDay < 12 ? "am" : "pm"));
                             setTime=date_1.getText().toString()+" "+utcTime;
                            Log.e("setTime",setTime);


                        } else {
                            Toast.makeText(getActivity(), "Please select valid Time", Toast.LENGTH_LONG).show();
                            date_1.setVisibility(View.INVISIBLE);
                        }

                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }


    private void openTimePicker2() {

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
                            date_2.setVisibility(View.VISIBLE);
                            //it's after current
                            int hour = hourOfDay % 12;
                            date_2.setText(selected_date2 + " " + String.format("%02d:%02d %s", hour == 0 ? 12 : hour,
                                    minute, hourOfDay < 12 ? "am" : "pm"));

                            String endTime=date_2.getText().toString()+" "+utcTime;
                            Log.e("endTime",endTime);
                        } else {
                            //it's before current'
                            Toast.makeText(getActivity(), "Please select Invalid Time", Toast.LENGTH_LONG).show();
                            date_2.setVisibility(View.INVISIBLE);
                        }

                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

}
