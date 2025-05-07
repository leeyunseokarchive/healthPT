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

import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

public class HomeFragment extends Fragment {

    private TextView peopleCountText;
    private TextView totalCapacityText;
    private ImageView gymcomplexImageView;

    private ImageView QRCodeImage;//홈 화면에 나올 QR코드 이미지
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

        setPeopleCount(currentCount);
        updateUIWithCount(currentCount);
        generateQRCode("userID");

        return view;
    }

    private void setPeopleCount(int count) {
        currentCount = count;
        totalCapacityText.setText(String.format("/%d명", totalCapacity));
        peopleCountText.setText(String.valueOf(currentCount));
    }

    private void updateUIWithCount(int count) {//퍼센트 단위로 바꿔놨습니다 by 장윤상

        if ((float) count / totalCapacity <= 0.3) {
        } else if ((float)count / totalCapacity <= 0.6) {
            gymcomplexImageView.setImageResource(R.drawable.common);
        } else {
            gymcomplexImageView.setImageResource(R.drawable.complex);
        }
    }

    private void generateQRCode(String data) {//QR 코드 생성함수, QR 코드 크기 조정하려면 try문 두번째 줄에 뒤 쪽 숫자 바꿔주시고 xml파일도 따로 수정해야됩니다
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(data, BarcodeFormat.QR_CODE, 150, 150);
            QRCodeImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }


    }

}

