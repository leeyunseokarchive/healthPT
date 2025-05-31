package com.example.healthpt;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.Mac;

public class WorkFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_work, container, false);

        boolean savedState = stateManager.getInstance(requireContext()).isSwitchOn();

        RecyclerView categoryRecyclerView = view.findViewById(R.id.categoryRecyclerView);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));


        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setReverseLayout(false);
        layoutManager.setStackFromEnd(false);
        categoryRecyclerView.setLayoutManager(layoutManager);

        // 카테고리별 기구 리스트 데이터 준비
        List<Machine> chestMachines = new ArrayList<>();
        chestMachines.add(new Machine("벤치 프레스", "보통", R.drawable.bench_image));
        chestMachines.add(new Machine("덤벨프레스", "혼잡", R.drawable.dubel_press_image));
        chestMachines.add(new Machine("펙덱플라이", "쾌적", R.drawable.peckdeck_fly_image));
        chestMachines.add(new Machine("체스트플라이", "보통", R.drawable.chest_fly_image));
        chestMachines.add(new Machine("케이블크로스오버", "혼잡", R.drawable.cable_crossover_image));


        List<Machine> legMachines = new ArrayList<>();
        legMachines.add(new Machine("레그 프레스", "쾌적", R.drawable.leg_press_image));
        legMachines.add(new Machine("레그 익스텐션", "보통", R.drawable.leg_extension_image));
        legMachines.add(new Machine("파워프레스", "혼잡", R.drawable.power_press_image));
        legMachines.add(new Machine("라잉레그컬", "쾌적", R.drawable.lying_legcurl_image));
        legMachines.add(new Machine("힙 어덕션", "혼잡", R.drawable.heap_aduction_image));

        List<Machine> backMachines = new ArrayList<>();
        backMachines.add(new Machine("랫풀다운", "보통", R.drawable.let_pulldown_image));
        backMachines.add(new Machine("시티드로우", "혼잡", R.drawable.seated_row_image));
        backMachines.add(new Machine("로우풀리", "쾌적", R.drawable.row_pully_image));
        backMachines.add(new Machine("로우로우머신", "보통", R.drawable.rowrow_machine_image));
        backMachines.add(new Machine("어시스트 풀업", "쾌적", R.drawable.assist_pullup_image));

        List<Machine> shoulderMachines =  new ArrayList<>();
        shoulderMachines.add(new Machine("숄더프레스", "쾌적", R.drawable.shoulder_press_image));
        shoulderMachines.add(new Machine("리어델트 머신", "보통", R.drawable.rear_delt_image));
        shoulderMachines.add(new Machine("리터럴 레이즈", "보통", R.drawable.lateral_raise_image));
        shoulderMachines.add(new Machine("케이블 숄더 프레스", "쾌적", R.drawable.cableshoulder_press_image));
        shoulderMachines.add(new Machine("아놀드 프레스", "보통", R.drawable.anold_press_image));


        List<Category> categories = new ArrayList<>();
        categories.add(new Category("가슴 운동기구", chestMachines));
        categories.add(new Category("하체 운동기구", legMachines));
        categories.add(new Category("등 운동기구", backMachines));
        categories.add(new Category("어깨 운동기구", shoulderMachines));

        CategoryAdapter categoryAdapter = new CategoryAdapter(
                requireContext(),
                categories,
                machine -> {
                    if(savedState) new AlertDialog.Builder(requireContext())
                            .setTitle(machine.getName())
                            .setMessage("혼잡도: " + machine.getCrowdLevel())
                            .setPositiveButton("확인", null)
                            .show();
                }
        );
        categoryRecyclerView.setAdapter(categoryAdapter);
        categoryRecyclerView.setAdapter(categoryAdapter);
        return view;
    }
}