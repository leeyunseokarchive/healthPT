package com.example.healthpt;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;



public class CalendarFragment extends Fragment {
    private MaterialCalendarView calendarView;//달력 뷰
    private Button checkInButton;//출석 체크 버튼

    // 출석 날짜 저장용
    private Set<CalendarDay> attendanceDays = new HashSet<>();
    private manageAttendance attendanceManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // fragment_calendar.xml 레이아웃을 inflate
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        calendarView = view.findViewById(R.id.calendarview);
        checkInButton = view.findViewById(R.id.checkInButton);

        attendanceManager = new manageAttendance(requireContext());
        // 테스트용 출석 날짜

        calendarView.addDecorator(new AttendanceDecorator(attendanceDays));


        // 출석 날짜 불러오기
        attendanceManager.loadAttendance(dateStrings -> {
            for (String date : dateStrings) {
                try {
                    Date parsed = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(date);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(parsed);
                    CalendarDay calendarDay = CalendarDay.from(
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH) + 1,
                            calendar.get(Calendar.DAY_OF_MONTH)
                    );
                    attendanceDays.add(calendarDay);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            calendarView.addDecorator(new AttendanceDecorator(attendanceDays));
        });

        //출석 체크는 아직 서버 연동 못해놔서 하는대로 올리겠슴미다 by 장윤상
        //임시로 파이어 베이스 로그인해서 연동하도록 만들어 놨습니다 by 장윤상
        checkInButton.setOnClickListener(view1 -> {//출석 체크 버튼 누를 경우
            Calendar calendar = Calendar.getInstance();
            CalendarDay today = CalendarDay.from(//오늘 날짜 확인
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1, // 0-based라 +1
                    calendar.get(Calendar.DAY_OF_MONTH)


            );
            if (attendanceDays.contains(today)) {
                Toast.makeText(requireContext(), "이미 출석했습니다!", Toast.LENGTH_SHORT).show();
                return;
            }
            // 출석일 추가
            attendanceManager.markTodayAttendance();
            attendanceDays.add(today);

            // 데코레이터 갱신
            calendarView.invalidateDecorators();
        });
    }

    // 출석 데코레이터
    private class AttendanceDecorator implements DayViewDecorator {

        private final Set<CalendarDay> dates;
        private final Drawable highlightDrawable;

        public AttendanceDecorator(Set<CalendarDay> dates) {
            this.dates = dates;
            // 동그란 배경 drawable, res -> drawble 파일에 있으니 맘에 안들면 바꾸쇼 by 장윤상
            highlightDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.circle_background);
        }

        @Override
        public boolean shouldDecorate(@NonNull CalendarDay day) {
            return dates.contains(day);
        }

        @Override
        public void decorate(@NonNull DayViewFacade view) {
            view.setBackgroundDrawable(highlightDrawable);
        }
    }


}

