package com.example.healthpt;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class manageAttendance {
    private final FirebaseFirestore db;
    private final FirebaseUser user;
    private final Context context;
    private final String userId;

    public manageAttendance(Context context) {
        this.context = context;
        this.db = FirebaseFirestore.getInstance();
        this.user = FirebaseAuth.getInstance().getCurrentUser();
        this.userId = "testing";
    }

    public void markTodayAttendance() {
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        DocumentReference docRef = db.collection("attendance").document(userId);

        Map<String, Object> data = new HashMap<>();
        data.put(today, true);

        docRef.set(data, SetOptions.merge())
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(context, "출석 체크 완료!", Toast.LENGTH_SHORT).show()
                )
                .addOnFailureListener(e ->
                        Toast.makeText(context, "저장 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }


    // 출석 확인
    public void hasCheckedIn(String date, AttendanceCheckCallback callback) {
        DocumentReference docRef = db.collection("attendance").document(userId);
        docRef.get()
                .addOnSuccessListener(snapshot -> {
                    Boolean checked = snapshot.getBoolean(date);
                    callback.onResult(Boolean.TRUE.equals(checked));
                })
                .addOnFailureListener(e -> callback.onResult(false));
    }

    private String getTodayDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }

    // 출석 데이터 전체를 불러올 때
    public interface AttendanceListCallback {
        void onLoaded(Set<String> dateStrings);
    }

    // 출석 여부만 확인할 때
    public interface AttendanceCheckCallback {
        void onResult(boolean hasCheckedIn);
    }

    public void loadAttendance(AttendanceListCallback callback) {
        DocumentReference docRef = db.collection("attendance").document(userId);
        docRef.get().addOnSuccessListener(snapshot -> {
            Set<String> dates = new HashSet<>();
            Map<String, Object> data = snapshot.getData();
            if (data != null) {
                for (String key : data.keySet()) {
                    dates.add(key);
                }
            }
            callback.onLoaded(dates);
        });
    }

    public void getTotalAttendanceCount(AttendanceCountCallback callback) {
        loadAttendance(dateStrings -> {
            int count = dateStrings.size();
            callback.onCountLoaded(count);
        });
    }

    public interface AttendanceCountCallback {
        void onCountLoaded(int count);
    }


}
