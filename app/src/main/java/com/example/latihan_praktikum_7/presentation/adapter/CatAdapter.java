package com.example.latihan_praktikum_7.presentation.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.latihan_praktikum_7.R;
import com.example.latihan_praktikum_7.data.entity.Cat;
import java.util.List;

public class CatAdapter extends RecyclerView.Adapter<CatAdapter.ViewHolder> {
    private List<Cat> catList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Cat cat);
    }

    public CatAdapter(List<Cat> catList, OnItemClickListener listener) {
        this.catList = catList;
        this.listener = listener;
    }

    public void setAnimals(List<Cat> cats) {
        this.catList = cats;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cat cat = catList.get(position);
        holder.tvName.setText("Nama: " + cat.getName());
        holder.tvOrigin.setText("Asal: " + cat.getOrigin());
        holder.tvDescription.setText("Deskripsi: " + cat.getDescription());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(cat);
            }
        });
    }

    @Override
    public int getItemCount() {
        return catList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvOrigin, tvDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvOrigin = itemView.findViewById(R.id.tv_origin);
            tvDescription = itemView.findViewById(R.id.tv_description);
        }
    }
}
