package com.pratik.covidvaccination.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pratik.covidvaccination.R;

import java.util.List;

public class SlotAdapter extends RecyclerView.Adapter<SlotAdapter.SlotViewHolder> {

    private Context context;
    private List<AdminAddSlotData> slotList;
    private OnItemClickListener listener;

    public SlotAdapter(Context context, List<AdminAddSlotData> slotList, OnItemClickListener listener) {
        this.context = context;
        this.slotList = slotList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_slot, parent, false);
        return new SlotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SlotViewHolder holder, int position) {
        AdminAddSlotData slot = slotList.get(position);
        holder.tvHospitalName.setText(slot.getName());
        holder.tvHospitalAddress.setText(slot.getAddress());
        holder.tvDistrict.setText(slot.getDistrict());
        holder.tvTaluka.setText(slot.getTaluka());
        holder.tvTotalVaccines.setText(String.valueOf(slot.getTotalVaccines()));
        holder.tvPinCode.setText(String.valueOf(slot.getPinCode()));
        holder.tvVaccineName.setText(slot.getVaccineName());


        holder.btnUpdate.setOnClickListener(view -> listener.onUpdateClick(position));
        holder.btnDelete.setOnClickListener(view -> listener.onDeleteClick(position));
    }

    @Override
    public int getItemCount() {
        return slotList.size();
    }

    public interface OnItemClickListener {
        void onUpdateClick(int position);
        void onDeleteClick(int position);
    }

    public static class SlotViewHolder extends RecyclerView.ViewHolder {

        TextView tvHospitalName, tvHospitalAddress, tvDistrict, tvTaluka, tvTotalVaccines, tvPinCode, tvVaccineName;
        Button btnUpdate, btnDelete;

        public SlotViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHospitalName = itemView.findViewById(R.id.tvHospitalName);
            tvHospitalAddress = itemView.findViewById(R.id.tvHospitalAddress);
            tvDistrict = itemView.findViewById(R.id.tvDistrict);
            tvTaluka = itemView.findViewById(R.id.tvTaluka);
            tvTotalVaccines = itemView.findViewById(R.id.tvTotalVaccines);
            tvPinCode = itemView.findViewById(R.id.tvPinCode);
            tvVaccineName = itemView.findViewById(R.id.tvVaccineName);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
