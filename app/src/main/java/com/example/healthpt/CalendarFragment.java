package com.example.healthpt;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.healthpt.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarFragment extends Fragment {

    private CalendarView calendarView;
    private TextView routineText;
    private Button checkInButton;

    //private AppDatabase db;
    private String selectedDate;

    public CalendarFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        calendarView = view.findViewById(R.id.calendarView);
        routineText = view.findViewById(R.id.routineText);
        checkInButton = view.findViewById(R.id.checkInButton);

       // db = Room.databaseBuilder(requireContext(),
                //AppDatabase.class, "routine-db").allowMainThreadQueries().build();

        selectedDate = getTodayDate();
        //loadRoutine(selectedDate);

        calendarView.setOnDateChangeListener((v, year, month, dayOfMonth) -> {
            selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
            //loadRoutine(selectedDate);
        });

        checkInButton.setOnClickListener(v -> {
            RoutineEntry entry = new RoutineEntry(selectedDate, true, getRoutineForDate(selectedDate));
           // db.routineDao().insert(entry);
            Toast.makeText(requireContext(), "출석 완료!", Toast.LENGTH_SHORT).show();
            //loadRoutine(selectedDate);
        });


        return  view;
    }

    /*
    private void loadRoutine(String date) {
        RoutineEntry entry = db.routineDao().getByDate(date);
        if (entry != null) {
            routineText.setText("루틴: " + entry.routine + "\n출석: " + (entry.isCheckedIn ? "✅" : "❌"));
        } else {
            routineText.setText("루틴: " + getRoutineForDate(date) + "\n출석: X");
        }
    }

     */

    private String getRoutineForDate(String date) {
        try {
            Date d = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(date);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            int day = c.get(Calendar.DAY_OF_WEEK);

            switch (day) {
                case Calendar.MONDAY: return "가슴 운동";
                case Calendar.TUESDAY: return "등 운동";
                case Calendar.WEDNESDAY: return "하체 운동";
                case Calendar.THURSDAY: return "어깨 운동";
                case Calendar.FRIDAY: return "팔 운동";
                case Calendar.SATURDAY: return "유산소";
                default: return "휴식";
            }
        } catch (Exception e) {
            return "루틴 없음";
        }
    }

    private String getTodayDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }



}

