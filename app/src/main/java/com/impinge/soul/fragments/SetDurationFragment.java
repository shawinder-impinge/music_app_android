package com.impinge.soul.fragments;

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

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.ikovac.timepickerwithseconds.TimePicker;
import com.impinge.soul.R;
import com.impinge.soul.activities.AudioPlayerViewActivity;
import com.impinge.soul.models.CategoryModel;
import com.impinge.soul.models.CategoryPojoModel;
import com.impinge.soul.models.User;
import com.impinge.soul.retrofit.APIClient;
import com.impinge.soul.retrofit.APIInterface;
import com.impinge.soul.retrofit.response.RestResponse;
import com.impinge.soul.util.PreferenceData;
import com.impinge.soul.util.SharedPreference;
import com.impinge.soul.util.Utility;
import com.impinge.soul.util.sweetdialog.Alert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import io.github.deweyreed.scrollhmspicker.ScrollHmsPicker;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetDurationFragment extends Fragment implements View.OnClickListener {
    Context context;
    ScrollHmsPicker start_timer;
    ArrayList<CategoryModel> spinner_list = new ArrayList<>();
    SmartMaterialSpinner nice_spinner;
    int Selected_id, total_minutes;
    TextView select_time, set_time, set;
    private Calendar calendar;
    private int mYear, mMonth, mDay, mHour, mMinute, hours, minutes, seconds;
    DatePickerDialog datePickerDialog;
    String selected_date, utcTime, final_time, current_time,current_utc_time;
    TimePicker time_picker;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.set_duration, container, false);
        context = container.getContext();
        findId(view);
        getCurrentDateTime();
        getAllCategories(view);
        calendar = Calendar.getInstance();
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        return view;
    }

    private void getCurrentDateTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        current_time = df.format(c.getTime());
        current_utc_time=localToUTC("yyyy-MM-dd HH:mm:ss",current_time);


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

                      try {
                          if (getActivity()!=null){
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
                          }
                      }catch (Exception e){
                          e.printStackTrace();
                      }

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


                hours = time_picker.getCurrentHour();
                minutes = time_picker.getCurrentMinute();
                seconds = time_picker.getCurrentSeconds();


                if (hours == 0) {
                    total_minutes = minutes;
                } else {
                    total_minutes = ((hours * 60) + (minutes));
                }


                if (Selected_id != 0) {
                    callsetDurationApi(total_minutes);
                } else {
                    Alert.showWarningAlert(getActivity(), "Please select category");
                }


                // getActivity().startActivity(new Intent(getActivity(), StartDurationActivity.class));

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


    private void callsetDurationApi(int total_minutes) {
        Utility utility = Utility.getInstance(getActivity());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("user", SharedPreference.fetchPrefenceData(context, PreferenceData.USER_ID));
        hashMap.put("category_id", Selected_id);
        hashMap.put("current_time", current_utc_time);
        hashMap.put("duration_in_min", String.valueOf(total_minutes));
        hashMap.put("timezone", "UTC");

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
                    Log.e("userrrrrrrrrrrr",response.body().toString());

                    utility.hideLoading();
                  //  getActivity().startActivity(new Intent(getActivity(), StartDurationActivity.class));
                    getActivity().startActivity(new Intent(getActivity(), AudioPlayerViewActivity.class).putExtra("duration","").putExtra("duration_category",Selected_id));

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




    public static String localToUTC(String dateFormat, String datesToConvert) {


        String dateToReturn = datesToConvert;

        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setTimeZone(TimeZone.getDefault());
        Date gmt = null;

        SimpleDateFormat sdfOutPutToSend = new SimpleDateFormat(dateFormat);
        sdfOutPutToSend.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {

            gmt = sdf.parse(datesToConvert);
            dateToReturn = sdfOutPutToSend.format(gmt);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateToReturn;
    }


}
