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

import java.util.ArrayList;
import java.util.List;

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

        List<Machine> legMachines = new ArrayList<>();
        legMachines.add(new Machine("레그 프레스", "한산", R.drawable.workout));
        legMachines.add(new Machine("레그 익스텐션", "보통", R.drawable.workout));

        List<Category> categories = new ArrayList<>();
        categories.add(new Category("가슴 운동기구", chestMachines));
        categories.add(new Category("하체 운동기구", legMachines));

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