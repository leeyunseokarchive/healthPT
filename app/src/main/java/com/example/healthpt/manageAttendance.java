package com.example.healthpt;

import android.content.Context;
import android.util.Log;
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

    // 내부 생성자: 로그인 완료된 사용자로 초기화
    public manageAttendance(Context context, FirebaseUser user) {
        this.context = context;
        this.db = FirebaseFirestore.getInstance();
        this.user = user;
        this.userId = user.getUid();
    }

    // 외부에서 사용하는 초기화 함수
    public static void init(Context context, AttendanceInitCallback callback) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            callback.onInitialized(new manageAttendance(context, currentUser));
        } else {
            auth.signInAnonymously()
                    .addOnSuccessListener(result -> {
                        FirebaseUser signedInUser = result.getUser();
                        if (signedInUser != null) {
                            callback.onInitialized(new manageAttendance(context, signedInUser));
                            Log.d("Firebase", "익명 로그인 성공!");
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "로그인 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    // 오늘 날짜로 출석 체크
    public void markTodayAttendance() {
        String today = getTodayDate();
        DocumentReference docRef = db.collection("attendance").document(userId);

        Map<String, Object> data = new HashMap<>();
        data.put(today, true);

        docRef.set(data, SetOptions.merge())
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(context, "출석 체크 완료!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(context, "저장 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    // 특정 날짜에 출석했는지 확인
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

    // 출석 일자 전체 불러오기
    public void loadAttendance(AttendanceListCallback callback) {
        DocumentReference docRef = db.collection("attendance").document(userId);
        docRef.get().addOnSuccessListener(snapshot -> {
            Set<String> dates = new HashSet<>();
            Map<String, Object> data = snapshot.getData();
            if (data != null) {
                dates.addAll(data.keySet());
            }
            callback.onLoaded(dates);
        });
    }

    // 출석 일 수 구하기
    public void getTotalAttendanceCount(AttendanceCountCallback callback) {
        loadAttendance(dateStrings -> {
            int count = dateStrings.size();
            callback.onCountLoaded(count);
        });
    }

    // 초기화 콜백 인터페이스
    public interface AttendanceInitCallback {
        void onInitialized(manageAttendance manager);
    }

    // 출석 날짜 리스트 콜백
    public interface AttendanceListCallback {
        void onLoaded(Set<String> dateStrings);
    }

    // 특정 날짜 출석 여부 확인 콜백
    public interface AttendanceCheckCallback {
        void onResult(boolean hasCheckedIn);
    }

    // 출석 개수 콜백
    public interface AttendanceCountCallback {
        void onCountLoaded(int count);
    }
}
