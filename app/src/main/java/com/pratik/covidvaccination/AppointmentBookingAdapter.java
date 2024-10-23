package com.pratik.covidvaccination;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pratik.covidvaccination.admin.AdminAddSlotData;

import java.util.List;

public class AppointmentBookingAdapter extends RecyclerView.Adapter<AppointmentBookingAdapter.SlotViewHolder> {

    private Context context;
    private List<AdminAddSlotData> slotList;
    private OnBookClickListener onBookClickListener;

    public AppointmentBookingAdapter(Context context, List<AdminAddSlotData> slotList, OnBookClickListener onBookClickListener) {
        this.context = context;
        this.slotList = slotList;
        this.onBookClickListener = onBookClickListener;
    }

    @NonNull
    @Override
    public SlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_slot_booking, parent, false);
        return new SlotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SlotViewHolder holder, int position) {
        AdminAddSlotData slot = slotList.get(position);
        holder.tvHospitalName.setText("Hospital Name : "+slot.getName());
        holder.tvHospitalAddress.setText("Address : "+slot.getAddress());
        holder.tvTotalVaccines.setText("Available Vaccines : "+String.valueOf(slot.getTotalVaccines()));
        holder.btnBookAppointment.setOnClickListener(v -> onBookClickListener.onBookClick(position));
        holder.tvVaccineName.setText("Vaccine Name : "+slot.getVaccineName());
    }

    @Override
    public int getItemCount() {
        return slotList.size();
    }

    public static class SlotViewHolder extends RecyclerView.ViewHolder {

        TextView tvHospitalName, tvHospitalAddress, tvTotalVaccines,tvVaccineName;
        Button btnBookAppointment;

        public SlotViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHospitalName = itemView.findViewById(R.id.tvHospitalName);
            tvHospitalAddress = itemView.findViewById(R.id.tvHospitalAddress);
            tvTotalVaccines = itemView.findViewById(R.id.tvTotalVaccines);
            btnBookAppointment = itemView.findViewById(R.id.btnBookAppointment);
            tvVaccineName = itemView.findViewById(R.id.tvVaccineNameAA);
        }
    }
    public interface OnBookClickListener {
        void onBookClick(int position);
    }
}
