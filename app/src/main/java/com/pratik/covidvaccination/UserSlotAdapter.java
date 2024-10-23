package com.pratik.covidvaccination;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pratik.covidvaccination.admin.AdminAddSlotData;

import java.util.List;

public class UserSlotAdapter extends RecyclerView.Adapter<UserSlotAdapter.SlotViewHolder> {

    private Context context;
    private List<AdminAddSlotData> slotList;

    public UserSlotAdapter(Context context, List<AdminAddSlotData> slotList) {
        this.context = context;
        this.slotList = slotList;
    }

    @NonNull
    @Override
    public SlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_slot_user, parent, false);
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
    }

    @Override
    public int getItemCount() {
        return slotList.size();
    }

    public static class SlotViewHolder extends RecyclerView.ViewHolder {

        TextView tvHospitalName, tvHospitalAddress, tvDistrict, tvTaluka, tvTotalVaccines, tvPinCode, tvVaccineName;

        public SlotViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHospitalName = itemView.findViewById(R.id.tvHospitalName);
            tvHospitalAddress = itemView.findViewById(R.id.tvHospitalAddress);
            tvDistrict = itemView.findViewById(R.id.tvDistrict);
            tvTaluka = itemView.findViewById(R.id.tvTaluka);
            tvTotalVaccines = itemView.findViewById(R.id.tvTotalVaccines);
            tvPinCode = itemView.findViewById(R.id.tvPinCode);
            tvVaccineName = itemView.findViewById(R.id.tvVaccineName);
        }
    }
}
