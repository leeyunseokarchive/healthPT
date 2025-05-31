package com.example.healthpt;

import android.content.res.ColorStateList;
import android.graphics.Color;
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
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class CommunityFragment extends Fragment {

    protected Switch switchMate;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_community, container, false);

        switchMate = view.findViewById(R.id.switchMate);

        boolean savedState = stateManager.getInstance(requireContext()).isSwitchOn();
        switchMate.setChecked(savedState);

        switchMate.setOnCheckedChangeListener((buttonView, isChecked) -> {
            stateManager.getInstance(requireContext()).setSwitchState(isChecked);
            Toast.makeText(getContext(), isChecked ? "메이트 기능이 활성화 됐습니다!" : "메이트 기능이 비활성화 됐습니다..", Toast.LENGTH_LONG).show();
        });
        return view;
    }
}

