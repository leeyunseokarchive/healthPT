package com.example.healthpt;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MachineAdapter extends RecyclerView.Adapter<MachineAdapter.MachineViewHolder> {
    private List<Machine> machineList;
    private OnItemClickListener listener;  // 클릭 리스너 변수
    // 인터페이스 정의
    public interface OnItemClickListener {
        void onItemClick(Machine machine);
    }

    // 생성자에 리스너 추가
    public MachineAdapter(List<Machine> machineList, OnItemClickListener listener) {
        this.machineList = machineList;
        this.listener = listener;
    }

    public static class MachineViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView crowdTextView;
        public ImageView machineImageView;

        public MachineViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.machineNameTextView);
            crowdTextView = itemView.findViewById(R.id.machineCrowdTextView);
            machineImageView = itemView.findViewById(R.id.machineImageView);
        }

        // 클릭 리스너 연결 메서드 추가
        public void bind(Machine machine, OnItemClickListener listener) {
            nameTextView.setText(machine.getName());
            crowdTextView.setText("혼잡도: " + machine.getCrowdLevel());
            machineImageView.setImageResource(machine.getImageResId());

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    new AlertDialog.Builder(itemView.getContext())
                            .setTitle("확인")
                            .setMessage(machine.getName() + "을(를) 사용하고 계시는 분께\n메이트 신청을 보내시겠습니까?")
                            .setPositiveButton("예", (dialog, which) -> {
                                //listener.onItemClick(machine);  // 예를 누르면 원래 하려던 작업 실행
                            })
                            .setNegativeButton("아니오", null)  // 아무 일도 안 함
                            .show();
                }
            });
        }
    }

    @Override
    public MachineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exercise_equiment_item, parent, false);
        return new MachineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MachineViewHolder holder, int position) {
        Machine machine = machineList.get(position);

        holder.bind(machine, listener);  // bind에서 클릭 리스너 연결
    }

    @Override
    public int getItemCount() {
        return machineList.size();
    }
}
