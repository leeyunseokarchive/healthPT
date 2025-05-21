package com.example.healthpt;

import android.graphics.Bitmap;
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
import android.widget.ImageView;
import android.widget.TextView;


public class HomeFragment extends Fragment {



    private TextView peopleCountText;
    private TextView totalCapacityText;
    private ImageView gymcomplexImageView;

    private ImageView QRCodeImage;//홈 화면에 나올 QR코드 이미지

    private TextView attendance;
    // 실시간 인원 현황(데이터 연동 필요)
    private int currentCount = 70;

    private int totalCapacity = 150;//헬스장 수용인원 수

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        totalCapacityText = view.findViewById(R.id.totalCapacityText);
        peopleCountText = view.findViewById(R.id.peopleCountText);
        gymcomplexImageView = view.findViewById(R.id.gymcomplexView);
        QRCodeImage = view.findViewById(R.id.QRCodeImage);
        attendance = view.findViewById(R.id.attendance);


        setPeopleCount(currentCount);
        updateUIWithCount(currentCount);
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

}

