package com.musicapp.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.jarklee.materialdatetimepicker.time.RadialPickerLayout;
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
import com.musicapp.util.sweetdialog.Alert;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import io.github.deweyreed.scrollhmspicker.ScrollHmsPicker;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EndTimeFragment extends Fragment implements View.OnClickListener, com.jarklee.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener, com.jarklee.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener {

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
    String selected_date, selected_date2, setTime, endTime, utcTime;
    String current_date = "";
    String date_one, time_one, date_two, time_two, final_start_time, final_end_time;
    boolean isFirstDate = false, isSecondDate = false;

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

    @Override
    public void onAttach(@NonNull Context act) {
        super.onAttach(context);
        this.context = act;
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
                            if (getActivity() != null) {
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
                        } catch (Exception e) {
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
        hashMap.put("user", SharedPreference.fetchPrefenceData(getActivity(), PreferenceData.USER_ID));
        hashMap.put("category_id", String.valueOf(Selected_id));
//        hashMap.put("set_time", selected_date);
//        hashMap.put("end_time", selected_date2);
        hashMap.put("set_time", final_start_time);
        hashMap.put("end_time", final_end_time);
        hashMap.put("timezone", "UTC");

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
                    Toast.makeText(context, "Alarm set successfully", Toast.LENGTH_SHORT).show();
                    //getActivity().startActivity(new Intent(getActivity(), StartDurationActivity.class));

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


//    public boolean loadFragment(Fragment fragment, String start_hours, String start_minutes, String start_seconds) {
//        //switching fragment
//        if (fragment != null) {
//            Bundle bundle = new Bundle();
//            bundle.putString("hours", start_hours);
//            bundle.putString("min", start_minutes);
//            bundle.putString("second", start_seconds);
//            fragment.setArguments(bundle);
//            getActivity().getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.frame_layout, fragment)
//                    .commit();
//            return true;
//        }
//        return false;
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set:

                // getActivity().startActivity(new Intent(getActivity(), StartDurationActivity.class));

                if (Selected_id != 0) {

                    if (date_1.getText().toString().equalsIgnoreCase("00:00") || date_2.getText().toString().equalsIgnoreCase("00:00")) {
                        Alert.showWarningAlert(context, "Please select valid time");
                    } else {
                        callAddAlarmApi();
                    }

                } else {
                    Alert.showWarningAlert(context, "Please select category");
                }


//                Log.e("User_id_",SharedPreference.fetchPrefenceData(getActivity(), PreferenceData.USER_ID));
//                Log.e("User_selected_id", String.valueOf(Selected_id));
//                Log.e("User_selected_date",selected_date);
//                Log.e("User_selected_date2",selected_date2);


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
                isFirstDate = true;

                datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                        // adding the selected date in the edittext
                        //  date_1.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                        selected_date = date_1.getText().toString();
                        date_one = year + "-" + (month + 1) + "-" + dayOfMonth;

                        Log.e("sellllllll", selected_date);
                        Log.e("currrrrrrr", date_one);


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


                        //openTimePicker();

                        openNewTimePicker();

                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                datePickerDialog.show();

                //openNewDatePicker();

                break;
            case R.id.time2:
                isSecondDate = true;
                datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                        // adding the selected date in the edittext
                        //  date_2.setText(year + "-" + (month + 1) + "-" + dayOfMonth);

                        selected_date2 = date_2.getText().toString();
                        date_two = year + "-" + (month + 1) + "-" + dayOfMonth;

                        //openTimePicker2();
                        openNewTimePicker();


                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                datePickerDialog.show();

                break;
        }
    }

    private void openNewDatePicker() {
        Calendar now = Calendar.getInstance();
        com.jarklee.materialdatetimepicker.date.DatePickerDialog dpd = com.jarklee.materialdatetimepicker.date.DatePickerDialog.newInstance(
                EndTimeFragment.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH));

        dpd.show(getFragmentManager(), "Datepickerdialog");

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
                            setTime = date_1.getText().toString() + " " + utcTime;
                            Log.e("setTime", setTime);


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

                            String endTime = date_2.getText().toString() + " " + utcTime;
                            Log.e("endTime", endTime);
                        } else {
                            //it's before current'
                            Toast.makeText(getActivity(), "Please select Invalid Time", Toast.LENGTH_LONG).show();
                            date_2.setVisibility(View.INVISIBLE);
                        }

                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    @Override
    public void onDateSet(com.jarklee.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = year + "/" + (++monthOfYear) + "/" + dayOfMonth;
        Log.e("selected_date", date);
        openNewTimePicker();
    }

    private void openNewTimePicker() {
        Calendar now = Calendar.getInstance();
        com.jarklee.materialdatetimepicker.time.TimePickerDialog tpd = com.jarklee.materialdatetimepicker.time.TimePickerDialog.newInstance(
                EndTimeFragment.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false);

        tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Log.d("TimePicker", "Dialog was cancelled");
            }
        });
        tpd.show(getFragmentManager(), "Timepickerdialog");
    }


    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        String secondString = second < 10 ? "0" + second : "" + second;


        if (isFirstDate == true) {

            Calendar datetime = Calendar.getInstance();
            Calendar c = Calendar.getInstance();
            datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            datetime.set(Calendar.MINUTE, minute);

            time_one = hourString + ":" + minuteString + ":" + secondString;
            if (datetime.getTimeInMillis() >= c.getTimeInMillis()) {
                //it's after current
                int hour = hourOfDay % 12;
                date_1.setText(date_one + " " + String.format("%02d:%02d %s", hour == 0 ? 12 : hour,
                        minute, ":00"));
            } else {
                //it's before current'
                Toast.makeText(getActivity(), "Invalid Time", Toast.LENGTH_LONG).show();
                date_1.setText("00:00");
            }


            if (date_one != null && time_one != null) {
                //final_start_time = getDate(date_one + " " + time_one);
                Log.e("time_simple", date_one + " " + time_one);

                final_start_time = localToUTC("yyyy-MM-dd HH:mm:ss", date_one + " " + time_one);

                Log.e("time_converted", final_start_time);
            } else {
                Toast.makeText(context, "Please select valid date & time", Toast.LENGTH_SHORT).show();
            }
















         /*   time_one = hourString + ":" + minuteString + ":" + secondString;
            date_1.setText(date_one + " " + time_one);

            if (date_one != null && time_one != null) {
                //final_start_time = getDate(date_one + " " + time_one);
                Log.e("time_simple",date_one + " " + time_one);

                final_start_time = localToUTC("yyyy-MM-dd HH:mm:ss",date_one + " " + time_one);

                Log.e("time_converted",final_start_time);
            } else {
                Toast.makeText(context, "please select valid date & time", Toast.LENGTH_SHORT).show();
            }*/


            isFirstDate = false;


        } else if (isSecondDate == true) {
            time_two = hourString + ":" + minuteString + ":" + secondString;
            date_2.setText(date_two + " " + time_two);

            if (date_two != null && time_two != null) {


                // final_end_time = getDate(date_two + " " + time_two);
                final_end_time = localToUTC("yyyy-MM-dd HH:mm:ss", date_two + " " + time_two);

                Log.e("selected_date_end", final_end_time);
            } else {
                Toast.makeText(context, "Please select valid date & time", Toast.LENGTH_SHORT).show();
            }
            isSecondDate = false;
        }


    }


    private String getDate(String ourDate) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date value = formatter.parse(ourDate);

            // SimpleDateFormat dateFormatter = new SimpleDateFormat("MM-dd-yyyy HH:mm"); //this format changeable
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //this format changeable
            dateFormatter.setTimeZone(TimeZone.getDefault());
            ourDate = dateFormatter.format(value);

            //Log.d("ourDate", ourDate);
        } catch (Exception e) {
            ourDate = "00-00-0000 00:00";
        }
        return ourDate;
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


    // TODO: 26-09-2021 invalid time code

   /* Calendar datetime = Calendar.getInstance();
    Calendar c = Calendar.getInstance();
                    datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    datetime.set(Calendar.MINUTE, minute);
                    if (datetime.getTimeInMillis() >= c.getTimeInMillis()) {
        //it's after current
        int hour = hourOfDay % 12;
        btnPickStartTime.setText(String.format("%02d:%02d %s", hour == 0 ? 12 : hour,
                minute, hourOfDay < 12 ? "am" : "pm"));
    } else {
        //it's before current'
        Toast.makeText(getApplicationContext(), "Invalid Time", Toast.LENGTH_LONG).show();
    }*/


}
