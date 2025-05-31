package com.example.healthpt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<Category> categoryList;
    private Context context;
    private MachineAdapter.OnItemClickListener machineClickListener;
    public CategoryAdapter(Context context, List<Category> categoryList,
                           MachineAdapter.OnItemClickListener machineClickListener) {
        this.context = context;
        this.categoryList = categoryList;
        this.machineClickListener = machineClickListener;
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        public TextView categoryTitle;
        public RecyclerView machineRecyclerView;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            categoryTitle = itemView.findViewById(R.id.categoryTitle);
            machineRecyclerView = itemView.findViewById(R.id.machineRecyclerView);
        }
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_categories, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.categoryTitle.setText(category.getCategoryName());

        // 기구 RecyclerView는 가로 스크롤
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        holder.machineRecyclerView.setLayoutManager(layoutManager);

        // 기구 어댑터 연결
        MachineAdapter machineAdapter = new MachineAdapter(category.getMachineList(), machineClickListener);
        holder.machineRecyclerView.setAdapter(machineAdapter);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}
