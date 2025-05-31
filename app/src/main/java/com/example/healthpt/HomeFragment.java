package com.example.healthpt;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;


public class HomeFragment extends Fragment {



    private TextView peopleCountText;
    private TextView totalCapacityText;
    private ImageView gymcomplexImageView;
    private Button NFC;
    private Button beaconFinder;

    private ImageView QRCodeImage;//홈 화면에 나올 QR코드 이미지

    private TextView attendance;
    // 실시간 인원 현황(데이터 연동 필요)
    private int currentCount = 70;

    private int totalCapacity = 150;//헬스장 수용인원 수

    private RelativeLayout qrCodeLayout;
    private GestureDetector gestureDetector;

    private int attendanceDays = 0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        totalCapacityText = view.findViewById(R.id.totalCapacityText);
        peopleCountText = view.findViewById(R.id.peopleCountText);
        gymcomplexImageView = view.findViewById(R.id.gymcomplexView);
        attendance = view.findViewById(R.id.attendance);
        NFC = view.findViewById(R.id.NFC);
        qrCodeLayout = view.findViewById(R.id.qrCodeLayout);
        beaconFinder = view.findViewById(R.id.beaconFinder);


        manageAttendance manageAttendance;


        setPeopleCount(currentCount);
        updateUIWithCount(currentCount);

        /*ImageView qrCodeImageView = view.findViewById(R.id.qrCodeImageView);//QR 코드 생성
        Bitmap qrBitmap = generateQRCode("Testing", 400);
        if (qrBitmap != null) {
            qrCodeImageView.setImageBitmap(qrBitmap);
        }*/

        FirebaseFirestore db = FirebaseFirestore.getInstance();//파이어베이스 객체

        NFC.setOnClickListener(v -> {//NFC 버튼 눌렀을때, 파이어베이스로 정보 이동
            Map<String, Object> data = new HashMap<>();
            data.put("testKey", "임시 데이터");
            data.put("timestamp", System.currentTimeMillis());

            db.collection("testing_data")
                    .document("testing_data")  // 문서 이름 직접 지정
                    .set(data)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(requireContext(), "저장 성공!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(requireContext(), "저장 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });

            Intent intent = new Intent(getActivity(), ExerciseTimerActivity.class);
            startActivity(intent);
        });

        beaconFinder.setOnClickListener(v->{
            Toast.makeText(requireContext(), "비콘 찾는중", Toast.LENGTH_SHORT);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                // 여기에 비콘 발견 시 동작 넣기
                Toast.makeText(requireContext(), "✅ 비콘 발견!", Toast.LENGTH_SHORT).show();

                // 예: 출석 체크 처리
                // 직접 만든 함수로 Firestore 저장 등
            }, 2000); // 2초 지연
        });



        /*gestureDetector = new GestureDetector(requireContext(), new GestureDetector.SimpleOnGestureListener() {//슬라이드 확인자
            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();

                if (Math.abs(diffY) > Math.abs(diffX)) {
                    if (diffY < -SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        // 위로 슬라이드 -> QR코드 보여주기
                        showQRCode();
                        return true;
                    } else if (diffY > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        // 아래로 슬라이드 -> QR코드 숨기기
                        hideQRCode();
                        return true;
                    }
                }
                return false;
            }

        });*/
        view.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));

        manageAttendance manager = new manageAttendance(requireContext());
        manager.getTotalAttendanceCount(count -> {
            // count는 출석한 일 수
            //Log.d("출석 일수", count + "일 출석했습니다.");
            attendance.setText("연속 출석 일자: " + count + "일");
        });

        return view;
    }

    private void setPeopleCount(int count) {
        currentCount = count;
        totalCapacityText.setText(String.format("/%d명", totalCapacity));
        peopleCountText.setText(String.valueOf(currentCount));
    }

    private void updateUIWithCount(int count) {//퍼센트 단위로 바꿔놨습니다 by 장윤상

        if ((float) count / totalCapacity <= 0.3) {
            gymcomplexImageView.setImageResource(R.drawable.free);
        } else if ((float)count / totalCapacity <= 0.6) {
            gymcomplexImageView.setImageResource(R.drawable.common);
        } else {
            gymcomplexImageView.setImageResource(R.drawable.complex);
        }
    }

    /*public Bitmap generateQRCode(String text, int size) {//QR코드 생성 함수
        try {
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, size, size);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            return barcodeEncoder.createBitmap(bitMatrix);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void showQRCode() {//QR코드 보여주는 함수
        if (qrCodeLayout.getVisibility() == View.GONE) {
            qrCodeLayout.setVisibility(View.VISIBLE);

            // 높이값 가져오기 위해 post() 사용
            qrCodeLayout.post(() -> {
                qrCodeLayout.setTranslationY(qrCodeLayout.getHeight());
                qrCodeLayout.animate()
                        .translationY(0)
                        .setDuration(300)
                        .start();
            });
        }
    }

    private void hideQRCode() {//QR코드 숨기는 함수
        if (qrCodeLayout.getVisibility() == View.VISIBLE) {
            qrCodeLayout.animate()
                    .translationY(qrCodeLayout.getHeight())
                    .setDuration(300)
                    .withEndAction(() -> qrCodeLayout.setVisibility(View.GONE))
                    .start();
        }
    }*/
}

