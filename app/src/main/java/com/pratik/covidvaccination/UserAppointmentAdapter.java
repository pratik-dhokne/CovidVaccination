package com.pratik.covidvaccination;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pratik.covidvaccination.R;
import com.pratik.covidvaccination.ReadWriteUserDetails;
import com.pratik.covidvaccination.UserBookAppointmentActivity;
import com.pratik.covidvaccination.admin.AdminAddSlotData;
import com.pratik.covidvaccination.admin.AdminVacStatus;
import com.pratik.covidvaccination.admin.AppointmentAdapter;

import java.util.List;

public class UserAppointmentAdapter extends RecyclerView.Adapter<UserAppointmentAdapter.AppointmentViewHolder> {

    private Context context;
    private List<ReadWriteUserDetails> appointmentList;
    private List<AdminAddSlotData> appointmentList1;

    private List<AdminVacStatus> adminVacStatusList;

    public UserAppointmentAdapter(Context context, List<ReadWriteUserDetails> appointmentList, List<AdminAddSlotData> appointmentList1, List<AdminVacStatus> adminVacStatusList) {
        this.context = context;
        this.appointmentList = appointmentList;
        this.appointmentList1 = appointmentList1;
        this.adminVacStatusList = adminVacStatusList;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_appointment, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        ReadWriteUserDetails appointment = appointmentList.get(position);
        AdminAddSlotData slotData = appointmentList1.get(position);
        AdminVacStatus adminVacStatus = adminVacStatusList.get(position);

        holder.tvSlotId.setText("Slot ID: " + slotData.getId());
        holder.tvHospitalName.setText("" + slotData.getName());
        holder.tvHospitalAddress.setText("Hospital Address: " + slotData.getAddress());
        holder.tvVaccineName.setText("Vaccine Name: " + slotData.getVaccineName());
        holder.tvUserName.setText("Name : "+appointment.getFullname());
        holder.tvStatus.setText(""+adminVacStatus.getStatus());
    }

    @Override
    public int getItemCount() {
        return Math.min(appointmentList.size(), appointmentList1.size());
    }

    public static class AppointmentViewHolder extends RecyclerView.ViewHolder {

        TextView tvSlotId, tvHospitalName, tvHospitalAddress, tvVaccineName,tvUserName,tvStatus;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSlotId = itemView.findViewById(R.id.tvSlotIduser);
            tvHospitalName = itemView.findViewById(R.id.tvHospitalNameuser);
            tvHospitalAddress = itemView.findViewById(R.id.tvHospitalAddressuser);
            tvVaccineName = itemView.findViewById(R.id.tvVaccineNameuser);
            tvUserName = itemView.findViewById(R.id.tvPatientNameuser);

            tvStatus = itemView.findViewById(R.id.tvstatus);
        }
    }
}

